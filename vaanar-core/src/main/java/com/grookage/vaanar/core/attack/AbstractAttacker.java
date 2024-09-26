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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@AllArgsConstructor
@Data
@NoArgsConstructor
public abstract class AbstractAttacker implements Attacker {

    private String name;
    private static final Map<String, AttackExecutor> executors = new ConcurrentHashMap<>();

    public String name() {
        return getAttackProperties().getName();
    }

    public synchronized String setupAttack() {
        final var that = this;
        final var executor = new AttackExecutor(() -> that);
        executor.start();
        final var attackId = UUID.randomUUID().toString();
        executors.put(attackId, executor);
        return attackId;
    }

    public void cancelAttack(final String attackId) {
        final var attackExecutor = executors.get(attackId);
        if (null != attackExecutor) {
            attackExecutor.stop();
        }
    }

    public void cancelAllAttacks() {
        executors.values().forEach(AttackExecutor::stop);
    }
}
