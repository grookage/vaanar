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

import com.grookage.vaanar.core.ResourceHelper;
import com.grookage.vaanar.core.attack.Attacker;
import com.grookage.vaanar.core.attack.custom.CustomAttackProperties;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.attack.exception.ExceptionAttackProperties;
import com.grookage.vaanar.core.attack.exception.ExceptionAttacker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class AttackRegistryUtilsTest {

    @Test
    @SneakyThrows
    void testAttackRegistry() {
        var attackConfiguration = ResourceHelper.getResource(
                "attackConfigurationDisabled.json",
                AttackConfiguration.class
        );
        var registry = AttackRegistryUtils.getRegistry(
                attackConfiguration, null
        );
        Assertions.assertTrue(registry.attackers().isEmpty());
        attackConfiguration = ResourceHelper.getResource(
                "attackConfiguration.json",
                AttackConfiguration.class
        );
        registry = AttackRegistryUtils.getRegistry(
                attackConfiguration, null
        );
        Assertions.assertFalse(registry.attackers().isEmpty());
        registry.getRegistry().values().forEach(attacker -> {
            Assertions.assertTrue(attacker.getExecutors().isEmpty());
        });
    }

    @Test
    @SneakyThrows
    void testAttackRegistryWithCustomFactory() {
        final var attackConfiguration = ResourceHelper.getResource(
                "attackConfiguration.json",
                AttackConfiguration.class
        );
        final var registry = AttackRegistryUtils.getRegistry(
                attackConfiguration, new TestableCustomFactory()
        );
        Assertions.assertFalse(registry.attackers().isEmpty());
        registry.getRegistry().values().forEach(attacker -> {
            Assertions.assertTrue(attacker.getExecutors().isEmpty());
        });
        final var attacker = registry.getAttacker("customAttack").orElse(null);
        Assertions.assertNotNull(attacker);
        Assertions.assertTrue(attacker instanceof ExceptionAttacker);

    }

    static class TestableCustomFactory implements CustomAttackerFactory {

        @Override
        public Optional<Attacker> getAttacker(String attackName, CustomAttackProperties attackProperties) {
            return attackName.equalsIgnoreCase("customAttack") ?
                    Optional.of(new ExceptionAttacker(new ExceptionAttackProperties())) : Optional.empty();
        }
    }
}
