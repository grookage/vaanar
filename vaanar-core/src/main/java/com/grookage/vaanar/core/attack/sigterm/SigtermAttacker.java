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

package com.grookage.vaanar.core.attack.sigterm;

import com.grookage.vaanar.core.attack.AbstractAttacker;
import com.grookage.vaanar.core.attack.AttackProcessor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Getter
public class SigtermAttacker extends AbstractAttacker {

    private final SigtermAttackProperties attackProperties;

    public SigtermAttacker(SigtermAttackProperties attackProperties,
                           AttackProcessor attackProcessor) {
        super(attackProcessor);
        this.attackProperties = attackProperties;
    }

    @Override
    public void attack() {
        log.info("Vaanar : SIGTERM : Let's go Kaboom!");
        System.exit(2);
    }
}
