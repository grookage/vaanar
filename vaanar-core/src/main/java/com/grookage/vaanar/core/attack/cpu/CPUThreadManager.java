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

import com.grookage.vaanar.core.attack.AttackUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@RequiredArgsConstructor
public class CPUThreadManager {

    private final CPUAttackProperties attackProperties;
    private double previousLoad = 0.0;
    private List<CPUThread> runningThreads = new ArrayList<>();
    private List<CPUThread> stoppedThreads = new ArrayList<>();

    public void tick() {
        var currentLoad = AttackUtils.getProcessCPULoad();
        final var targetLoad = attackProperties.getTargetLoad();
        final var leeway = attackProperties.getLeeway();
        final var threadSleepDurationMs = attackProperties.getThreadSleepDurationMs();
        // only change things if we have new data
        if (currentLoad != previousLoad) {
            previousLoad = currentLoad;
            if (currentLoad < targetLoad) {
                CPUThread thread;
                if (stoppedThreads.isEmpty()) {
                    thread = new CPUThread("CPU Attacker " + runningThreads.size());
                    thread.start();
                } else {
                    thread = stoppedThreads.remove(0);
                    synchronized (thread) {
                        thread.resumeAttack();
                        thread.notify();
                    }
                }
                runningThreads.add(thread);
            }
        } else if (previousLoad > targetLoad + leeway && !runningThreads.isEmpty()) {
            final var thread = runningThreads.remove(0);
            thread.pauseAttack();
            stoppedThreads.add(thread);
        }

        try {
            Thread.sleep(threadSleepDurationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        runningThreads.addAll(stoppedThreads);
        runningThreads.forEach(thread -> {
            thread.interrupt();
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
