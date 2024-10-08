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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.grookage.vaanar.core.attack.cpu.CPUAttackProperties;
import com.grookage.vaanar.core.attack.criteria.AttackCriteria;
import com.grookage.vaanar.core.attack.custom.CustomAttackProperties;
import com.grookage.vaanar.core.attack.exception.ExceptionAttackProperties;
import com.grookage.vaanar.core.attack.latency.LatencyAttackProperties;
import com.grookage.vaanar.core.attack.memory.MemoryAttackProperties;
import com.grookage.vaanar.core.attack.sigterm.SigtermAttackProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CPUAttackProperties.class, name = "CPU"),
        @JsonSubTypes.Type(value = CustomAttackProperties.class, name = "CUSTOM"),
        @JsonSubTypes.Type(value = ExceptionAttackProperties.class, name = "EXCEPTION"),
        @JsonSubTypes.Type(value = MemoryAttackProperties.class, name = "MEMORY"),
        @JsonSubTypes.Type(value = LatencyAttackProperties.class, name = "LATENCY"),
        @JsonSubTypes.Type(value = SigtermAttackProperties.class, name = "SIGTERM")
})
public abstract class AttackProperties {
    @NotNull
    private AttackType type;
    private String name;
    private boolean enabled = false;
    private boolean repeatable = false;
    private boolean interceptable = false;
    private long initialDelayMs = 0L;
    private long executeAfterDelayMs = 0L;
    private long executeUntilTimeMs = 30000L;
    private AttackCriteria criteria = new AttackCriteria();

    public abstract <T> T accept(AttackPropertyAcceptor<T> acceptor);

}
