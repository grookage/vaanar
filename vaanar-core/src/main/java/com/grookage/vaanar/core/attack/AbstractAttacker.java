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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@AllArgsConstructor
@Data
@Slf4j
public abstract class AbstractAttacker implements Attacker {

    private static final Map<String, AttackExecutor> executors = new ConcurrentHashMap<>();
    private static final AttackPredicate ELIGIBILITY = new AttackPredicate();

    public String name() {
        return getAttackProperties().getName();
    }

    public synchronized void setupAttack() {
        final var attackProperties = getAttackProperties();
        if (!ELIGIBILITY.test(attackProperties)) return;
        final var that = this;
        final var executor = new AttackExecutor(() -> that);
        executor.start();
        final var attackId = UUID.randomUUID().toString();
        executors.put(attackId, executor);
    }

    @Override
    public Map<String, AttackExecutor> getExecutors() {
        return executors;
    }

}
