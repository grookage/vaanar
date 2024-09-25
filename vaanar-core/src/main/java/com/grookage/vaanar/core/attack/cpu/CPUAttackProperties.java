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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.AttackPropertyAcceptor;
import com.grookage.vaanar.core.attack.AttackType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CPUAttackProperties extends AttackProperties {
    private int holdLoadMs = 90000;
    private double targetLoad = 0.9;
    private double leeway = 0.05;
    private long threadSleepDurationMs = 500;

    public CPUAttackProperties(String name,
                               boolean active,
                               int holdLoadMs,
                               double targetLoad,
                               double leeway,
                               long threadSleepDurationMs) {
        super(AttackType.CPU, name, active);
        this.holdLoadMs = holdLoadMs;
        this.targetLoad = targetLoad;
        this.leeway = leeway;
        this.threadSleepDurationMs = threadSleepDurationMs;
    }

    @Override
    public <T> T accept(AttackPropertyAcceptor<T> acceptor) {
        return acceptor.accept(this);
    }
}
