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

package com.grookage.vaanar.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Stage;
import com.google.inject.matcher.Matchers;
import com.grookage.vaanar.core.VaanarEngine;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.attack.interceptor.AttackFunction;
import com.grookage.vaanar.core.registry.AttackConfiguration;
import com.grookage.vaanar.dw.VaanarBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.util.Optional;

@Slf4j
public class App extends Application<AppConfiguration> {


    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        final var vaanarBundle = new VaanarBundle<AppConfiguration>() {
            @Override
            protected AttackConfiguration getAttackConfiguration(AppConfiguration configuration) {
                return configuration.getAttackConfiguration();
            }

            @Override
            protected Optional<CustomAttackerFactory> getAdditionalAttackers(AppConfiguration configuration, Environment environment) {
                return Optional.empty();
            }
        };
        bootstrap.addBundle(vaanarBundle);
        final var guiceBundle = GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage()
                        .getName())
                .modules(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bindInterceptor(Matchers.any(), Matchers.annotatedWith(AttackFunction.class), vaanarBundle.getAttackInterceptor());
                    }

                    @Provides
                    public VaanarEngine getVaanarEngine() {
                        return vaanarBundle.getVaanarEngine();
                    }
                })
                .build(Stage.PRODUCTION);
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) {
        //NOOP
    }
}
