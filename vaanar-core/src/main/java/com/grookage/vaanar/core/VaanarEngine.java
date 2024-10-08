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

package com.grookage.vaanar.core;

import com.grookage.vaanar.core.attack.AttackProcessor;
import com.grookage.vaanar.core.attack.Attacker;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.registry.AttackConfiguration;
import com.grookage.vaanar.core.registry.AttackRegistry;
import com.grookage.vaanar.core.registry.AttackRegistryUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class VaanarEngine {

    private final AttackConfiguration attackConfiguration;
    private final CustomAttackerFactory attackerFactory;
    private final AttackRegistry attackRegistry;

    public VaanarEngine(AttackConfiguration attackConfiguration,
                        CustomAttackerFactory attackerFactory,
                        AttackProcessor attackProcessor) {
        this.attackConfiguration = attackConfiguration;
        this.attackerFactory = attackerFactory;
        this.attackRegistry = AttackRegistryUtils.getRegistry(attackConfiguration, attackerFactory, attackProcessor);
        this.start();
    }

    public void start() {
        log.info("Starting Monkey Business");
        attackRegistry.attackers()
                .stream()
                .filter(each -> !each.getAttackProperties().isInterceptable())
                .forEach(Attacker::setupAttack);
        log.info("Started Monkey Business");
    }
}
