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
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Getter
public class AttackExecutor {

    private final ScheduledExecutorService executorService;
    private final Supplier<Attacker> attackSupplier;
    private final AttackRunner attackRunner;
    private final AttackProperties attackProperties;
    private ScheduledFuture<?> executorFuture;

    @Builder
    public AttackExecutor(String attackId,
                          Supplier<Attacker> attackSupplier,
                          AttackProcessor attackProcessor) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.attackSupplier = attackSupplier;
        this.attackProperties = attackSupplier.get().getAttackProperties();
        this.attackRunner = new AttackRunner(attackId, attackProcessor);
    }

    public void start() {
        log.info("Starting Chaos for properties {}", attackProperties);
        if (attackProperties.isRepeatable()) {
            executorFuture = this.executorService.scheduleWithFixedDelay(attackRunner,
                    attackProperties.getInitialDelayMs(),
                    attackProperties.getExecuteAfterDelayMs(),
                    TimeUnit.MILLISECONDS);
            this.executorService.schedule(() -> {
                executorFuture.cancel(true);
            }, attackProperties.getExecuteUntilTimeMs(), TimeUnit.MILLISECONDS);
        } else {
            this.attackRunner.run();
        }
    }

    @AllArgsConstructor
    public class AttackRunner implements Runnable {

        private final String attackId;
        private final AttackProcessor attackProcessor;

        @Override
        public void run() {
            final var props = attackSupplier.get().getAttackProperties();
            attackProcessor.beforeAttack(attackId, props);
            try {
                attackSupplier.get().attack();
                log.info("[AttackRunner.update] Successfully Completed Attack for properties {}..", props);
            } catch (Exception e) {
                log.error("[AttackRunner.update] Error While executing attack or properties {}..", props);
            } finally {
                attackProcessor.afterAttack(attackId, props);
            }
        }
    }
}
