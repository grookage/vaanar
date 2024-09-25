/*
 * Copyright (c) 2024. Koushik R <rkoushik.14@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grookage.vaanar.dw;

import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.registry.AttackRegistry;
import com.grookage.vaanar.core.registry.AttackRegistryUtils;
import com.grookage.vaanar.core.scheduler.AttackConfiguration;
import com.grookage.vaanar.dw.health.VaanarHealthCheck;
import com.grookage.vaanar.dw.lifecycle.VaanarLifecycle;
import com.grookage.vaanar.dw.resources.VaanarResource;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Slf4j
public abstract class VaanarBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private AttackRegistry attackRegistry;

    protected abstract AttackConfiguration getAttackConfiguration(T configuration);

    protected abstract Optional<CustomAttackerFactory> getAdditionalAttackers(
            T configuration, Environment environment
    );

    protected List<VaanarHealthCheck> withHealthChecks(T configuration) {
        return List.of();
    }

    protected List<VaanarLifecycle> withLifecycleManagers(T configuration) {
        return List.of();
    }

    @Override
    public void run(T configuration, Environment environment) {
        final var attackConfiguration = getAttackConfiguration(configuration);
        if (null == attackConfiguration || !attackConfiguration.isEnableDestruction()) {
            log.info("No destruction configured. Exiting gracefully, nothing to do here");
        }

        final var attackProperties = null == attackConfiguration ? new ArrayList<AttackProperties>() :
                attackConfiguration.getAttackProperties();
        final var additionalAttackers = getAdditionalAttackers(configuration, environment).orElse(null);

        this.attackRegistry = AttackRegistryUtils.buildAttackRegistry(
                attackProperties, additionalAttackers
        );

        withLifecycleManagers(configuration)
                .forEach(lifecycle -> environment.lifecycle().manage(new Managed() {
                    @Override
                    public void start() {
                        lifecycle.start();
                    }

                    @Override
                    public void stop() {
                        lifecycle.stop();
                    }
                }));
        withHealthChecks(configuration)
                .forEach(healthCheck -> environment.healthChecks().register(healthCheck.getName(), healthCheck));
        environment.jersey().register(new VaanarResource(attackRegistry, additionalAttackers));
    }
}
