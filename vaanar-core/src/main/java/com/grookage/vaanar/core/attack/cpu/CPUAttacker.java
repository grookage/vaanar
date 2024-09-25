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

package com.grookage.vaanar.core.attack.cpu;

import com.google.common.base.Preconditions;
import com.grookage.vaanar.core.attack.AttackUtils;
import com.grookage.vaanar.core.attack.Attacker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@RequiredArgsConstructor
public class CPUAttacker implements Attacker {

    private final CPUAttackProperties attackProperties;
    private CPUThreadManager threadManager;

    @Override
    public void attack() {
        if (!active()) {
            log.warn("Attack Definition for name {} is not enabled. Ignoring this", name());
            return;
        }

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

    @Override
    public void initialize() {
        Preconditions.checkNotNull(attackProperties, "Attack Definition can't be null");
        Preconditions.checkNotNull(name(), "Name can't be null");

        this.threadManager = new CPUThreadManager(attackProperties);
    }
}
