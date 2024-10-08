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

package com.grookage.vaanar.core.attack;

import com.grookage.vaanar.core.attack.criteria.AttackPredicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultAttackProcessor implements AttackProcessor {
    private static final AttackPredicate ELIGIBILITY = new AttackPredicate();
    @Override
    public boolean eligible(AttackProperties properties) {
        return ELIGIBILITY.test(properties);
    }

    @Override
    public void beforeAttack(String attackId, AttackProperties attackProperties) {
        log.info("Started attack with attackId {} and attackProperties {}", attackId, attackProperties);
    }

    @Override
    public void afterAttack(String attackId, AttackProperties attackProperties) {
        log.info("Executed attack with attackId {} and attackProperties {}", attackId, attackProperties);
    }
}
