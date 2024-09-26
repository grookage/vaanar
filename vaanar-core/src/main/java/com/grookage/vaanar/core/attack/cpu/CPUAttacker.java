/*
 * Copyright 2019-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * {@Ref : https://github.com/codecentric/chaos-monkey-spring-boot}
 */

package com.grookage.vaanar.core.attack.cpu;

import com.grookage.vaanar.core.attack.AbstractAttacker;
import com.grookage.vaanar.core.attack.AttackUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CPUAttacker extends AbstractAttacker {

    private final CPUAttackProperties attackProperties;
    private final CPUThreadManager threadManager;

    public CPUAttacker(final CPUAttackProperties attackProperties) {
        this.attackProperties = attackProperties;
        this.threadManager = new CPUThreadManager(attackProperties);
    }

    @Override
    public void attack() {
        final var processLoad = AttackUtils.getCPULoad();
        if (processLoad < 0) {
            log.warn("No CPU Information Available. Exiting the attack for name {}", name());
            return;
        }

        log.info("Vaanar : Starting the CPU Attack");
        final var targetLoad = attackProperties.getTargetLoad();
        final var holdLoad = attackProperties.getHoldLoadMs();
        while (AttackUtils.getProcessCPULoad() < targetLoad) {
            threadManager.tick();
        }
        final var targetMs = System.currentTimeMillis() + holdLoad;
        while (targetMs > System.currentTimeMillis()) {
            threadManager.tick();
        }
        threadManager.stop();
    }
}
