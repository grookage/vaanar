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

package com.grookage.vaanar.core;

import com.grookage.vaanar.core.registry.AttackConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VaanarEngineTest {

    @Test
    @SneakyThrows
    void testVaanarEngineWithDestructionDisabled() {
        final var attackConfiguration = ResourceHelper.getResource(
                "attackConfigurationDisabled.json",
                AttackConfiguration.class
        );
        final var vaanarEngine = new VaanarEngine(attackConfiguration, null);
        Assertions.assertTrue(vaanarEngine.getAttackRegistry().attackers().isEmpty());
    }

    @Test
    @SneakyThrows
    void testVaanarEngineWithDestruction() {
        final var attackConfiguration = ResourceHelper.getResource(
                "attackConfiguration.json",
                AttackConfiguration.class
        );
        final var vaanarEngine = new VaanarEngine(attackConfiguration, null);
        Assertions.assertFalse(vaanarEngine.getAttackRegistry().attackers().isEmpty());
        vaanarEngine.getAttackRegistry().getRegistry().values().forEach(attacker -> {
            Assertions.assertTrue(attacker.getExecutors().isEmpty());
        });
    }
}
