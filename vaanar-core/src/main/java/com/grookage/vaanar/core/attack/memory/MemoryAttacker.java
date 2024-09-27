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

package com.grookage.vaanar.core.attack.memory;

import com.grookage.vaanar.core.attack.AbstractAttacker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class MemoryAttacker extends AbstractAttacker {

    private static final AtomicLong stolenMemory = new AtomicLong(0);
    private final MemoryAttackProperties attackProperties;

    public MemoryAttacker(MemoryAttackProperties attackProperties) {
        this.attackProperties = attackProperties;
    }

    @Override
    public void attack() {
        log.info("Vaanar : Starting the memory monkey!");
        final var memoryVector = new ArrayList<byte[]>();
        memoryHog(memoryVector);
        waitUntil(getAttackProperties().getHoldTimeMs());
        memoryVector.clear();
        Runtime.getRuntime().gc();
        log.info("Vaanar : Stopping the memory monkey!");
    }

    private void memoryHog(ArrayList<byte[]> memoryVector) {
        while (true) {
            final var limit = Runtime.getRuntime().maxMemory() * attackProperties.getTarget();
            final var allocationLimitBreached = Runtime.getRuntime().totalMemory() > Math.floor(limit);

            if (allocationLimitBreached) {
                log.debug("Can't allocate more memory. Monkey cry!");
                break;
            }
            final var amount = (int) (Runtime.getRuntime().freeMemory() * attackProperties.getIncrement());
            //Converting into megabytes
            final int size = Math.min(256 * 1024 * 1024, amount);
            final var slicedMemory = getChunkySlice(size);
            memoryVector.add(slicedMemory);
            waitUntil(attackProperties.getNextIncreaseWaitMs());
        }
    }

    private byte[] getChunkySlice(int size) {
        byte[] b = new byte[size];
        IntStream.iterate(0, idx -> idx < size, idx -> idx + 4096).forEach(idx -> b[idx] = 19);
        return b;
    }

    private void waitUntil(long ms) {
        final long startNano = System.nanoTime();
        var now = startNano;
        while (startNano + TimeUnit.MILLISECONDS.toNanos(ms) > now) {
            try {
                final var elapsed = TimeUnit.NANOSECONDS.toMillis(startNano - now);
                Thread.sleep(Math.min(100, ms - elapsed));
                now = System.nanoTime();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
