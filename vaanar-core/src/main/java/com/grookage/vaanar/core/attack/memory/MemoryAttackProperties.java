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

package com.grookage.vaanar.core.attack.memory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.AttackPropertyAcceptor;
import com.grookage.vaanar.core.attack.AttackType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemoryAttackProperties extends AttackProperties {
    private long holdTimeMs = 90000;
    private long nextIncreaseWaitMs = 1000;
    private double increment = 0.15;
    private double target = 0.25;

    public MemoryAttackProperties(final String name,
                                  final boolean enabled,
                                  final boolean repeatable,
                                  final boolean interceptable,
                                  long initialDelayMs,
                                  long executeAfterDelayMs,
                                  long executeUntilDelayMs,
                                  long holdTimeMs,
                                  long nextIncreaseWaitMs,
                                  long increment,
                                  long target) {
        super(AttackType.MEMORY, name, enabled, repeatable, interceptable,
                initialDelayMs, executeAfterDelayMs, executeUntilDelayMs);
        this.holdTimeMs = holdTimeMs;
        this.nextIncreaseWaitMs = nextIncreaseWaitMs;
        this.increment = increment;
        this.target = target;
    }

    @Override
    public <T> T accept(AttackPropertyAcceptor<T> acceptor) {
        return acceptor.accept(this);
    }
}

