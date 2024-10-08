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
import com.grookage.vaanar.core.attack.DefaultAttackProcessor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AttackRegistryTest {

    @Test
    @SneakyThrows
    void testAttackRegistry() {
        final var attackConfiguration = ResourceHelper.getResource(
                "attackConfiguration.json",
                AttackConfiguration.class
        );
        final var registry = AttackRegistryUtils.getRegistry(
                attackConfiguration, new AttackRegistryUtilsTest.TestableCustomFactory(),
                new DefaultAttackProcessor()
        );
        Assertions.assertNotNull(registry);
        Assertions.assertNotNull(registry.getAttacker("customAttack").orElse(null));
        Assertions.assertNull(registry.getAttacker("testAttack").orElse(null));
        Assertions.assertFalse(registry.getAttackProperties().isEmpty());
        Assertions.assertFalse(registry.attackers().isEmpty());
    }
}
