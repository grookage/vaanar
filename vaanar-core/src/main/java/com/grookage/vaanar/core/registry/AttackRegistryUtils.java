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

package com.grookage.vaanar.core.registry;


import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.AttackPropertyAcceptor;
import com.grookage.vaanar.core.attack.Attacker;
import com.grookage.vaanar.core.attack.cpu.CPUAttackProperties;
import com.grookage.vaanar.core.attack.cpu.CPUAttacker;
import com.grookage.vaanar.core.attack.custom.CustomAttackProperties;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.attack.exception.ExceptionAttackProperties;
import com.grookage.vaanar.core.attack.exception.ExceptionAttacker;
import com.grookage.vaanar.core.attack.latency.LatencyAttackProperties;
import com.grookage.vaanar.core.attack.latency.LatencyAttacker;
import com.grookage.vaanar.core.attack.memory.MemoryAttackProperties;
import com.grookage.vaanar.core.attack.memory.MemoryAttacker;
import com.grookage.vaanar.core.attack.sigterm.SigtermAttackProperties;
import com.grookage.vaanar.core.attack.sigterm.SigtermAttacker;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class AttackRegistryUtils {

    @Getter
    private static AttackRegistry attackRegistry = new AttackRegistry();

    public static void initialize(
            final List<AttackProperties> attackProperties,
            final CustomAttackerFactory additionalAttackers
    ) {
        if (null != attackProperties) {
            attackProperties.forEach(attackProperty -> {
                final var probableAttacker = getAttacker(attackProperty, additionalAttackers);
                probableAttacker.ifPresent(attacker -> attackRegistry.addAttacker(attackProperty.getName(), attacker));
            });
        }
    }

    public static Optional<Attacker> getAttacker(
            final AttackProperties attackProperty,
            final CustomAttackerFactory additionalAttackers
    ) {
        return attackProperty.accept(new AttackPropertyAcceptor<>() {
            @Override
            public Optional<Attacker> accept(CPUAttackProperties properties) {
                return Optional.of(new CPUAttacker(properties));
            }

            @Override
            public Optional<Attacker> accept(CustomAttackProperties properties) {
                return Optional.ofNullable(additionalAttackers).flatMap(attackers -> attackers.getAttacker(
                        properties.getName(),
                        properties
                ));
            }

            @Override
            public Optional<Attacker> accept(ExceptionAttackProperties properties) {
                return Optional.of(new ExceptionAttacker(properties));
            }

            @Override
            public Optional<Attacker> accept(LatencyAttackProperties properties) {
                return Optional.of(new LatencyAttacker(properties));
            }

            @Override
            public Optional<Attacker> accept(MemoryAttackProperties properties) {
                return Optional.of(new MemoryAttacker(properties));
            }

            @Override
            public Optional<Attacker> accept(SigtermAttackProperties properties) {
                return Optional.of(new SigtermAttacker(properties));
            }
        });
    }
}
