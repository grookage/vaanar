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

package com.grookage.vaanar.core.engine;

import com.grookage.vaanar.core.attack.Attacker;
import com.grookage.vaanar.core.registry.AttackRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Slf4j
public class VaanarEngine {

    private final AttackRegistry attackRegistry;
    private final AttackConfiguration attackConfiguration;

    public void start() {
        if (null == attackConfiguration || !attackConfiguration.isEnableDestruction()) {
            log.info("Oops, no monkey business is enabled. Tough luck. Exiting!");
        }
        attackRegistry.attackers().forEach(Attacker::setupAttack);
        log.info("Started Monkey Business");
    }

    public void stop() {
        log.info("Stopping the Monkey Business");
        attackRegistry.attackers().forEach(Attacker::cancelAllAttacks);
        log.info("Stopped the Monkey Business");
    }
}
