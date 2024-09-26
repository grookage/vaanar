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

package com.grookage.vaanar.core.attack.latency;

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
public class LatencyAttackProperties extends AttackProperties {

    private long latencyDurationMs = 1000;

    public LatencyAttackProperties(final String name,
                                   long initialDelayMs,
                                   long executeAfterDelayMs,
                                   long executeUntilDelayMs,
                                   long latencyDurationMs) {
        super(AttackType.LATENCY, name, initialDelayMs, executeAfterDelayMs, executeUntilDelayMs);
        this.latencyDurationMs = latencyDurationMs;
    }

    @Override
    public <T> T accept(AttackPropertyAcceptor<T> acceptor) {
        return acceptor.accept(this);
    }
}
