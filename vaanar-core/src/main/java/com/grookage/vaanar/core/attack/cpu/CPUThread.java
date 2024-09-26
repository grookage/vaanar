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

public class CPUThread extends Thread {

    private volatile boolean continueAttack = true;

    public CPUThread(final String name) {
        super(name);
    }

    public void pauseAttack() {
        continueAttack = false;
    }

    public void resumeAttack() {
        continueAttack = true;
    }

    @Override
    public void run() {
        var initialValue = 0;
        var incrementValue = 1;
        while (!interrupted()) {
            try {
                if (!continueAttack) {
                    synchronized (this) {
                        wait();
                    }
                }
                incrementValue = initialValue + incrementValue;
                initialValue = incrementValue - initialValue;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
