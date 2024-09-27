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

import com.grookage.vaanar.core.VaanarEngine;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.registry.AttackConfiguration;
import com.grookage.vaanar.dw.interceptors.AttackFunctionInterceptor;
import com.grookage.vaanar.dw.resources.VaanarResource;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@SuppressWarnings("unused")
@NoArgsConstructor
@Getter
@Slf4j
public abstract class VaanarBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private VaanarEngine vaanarEngine;
    private AttackFunctionInterceptor attackInterceptor;

    protected abstract AttackConfiguration getAttackConfiguration(T configuration);

    protected abstract Optional<CustomAttackerFactory> getAdditionalAttackers(
            T configuration, Environment environment
    );

    @Override
    public void run(T configuration, Environment environment) {
        final var attackConfiguration = getAttackConfiguration(configuration);
        if (null == attackConfiguration || !attackConfiguration.isEnableDestruction()) {
            log.info("No destruction configured. Exiting gracefully, nothing to do here");
            return;
        }

        this.vaanarEngine = new VaanarEngine(attackConfiguration,
                getAdditionalAttackers(configuration, environment).orElse(null));
        this.attackInterceptor = new AttackFunctionInterceptor(
                () -> vaanarEngine.getAttackRegistry()
        );
        environment.jersey().register(new VaanarResource(vaanarEngine));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }
}

