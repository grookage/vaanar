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
import com.grookage.vaanar.core.attack.AttackType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AttackConfigurationTest {

    @Test
    @SneakyThrows
    void testAttackConfiguration() {
        var attackConfiguration = ResourceHelper.getResource(
                "attackConfigurationDisabled.json",
                AttackConfiguration.class
        );
        Assertions.assertNotNull(attackConfiguration);
        Assertions.assertFalse(attackConfiguration.isEnableDestruction());
        Assertions.assertFalse(attackConfiguration.getAttackProperties().isEmpty());
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.CPU));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.MEMORY));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.LATENCY));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().noneMatch(each -> each.getType() == AttackType.EXCEPTION));
        attackConfiguration = ResourceHelper.getResource(
                "attackConfiguration.json",
                AttackConfiguration.class
        );
        Assertions.assertNotNull(attackConfiguration);
        Assertions.assertTrue(attackConfiguration.isEnableDestruction());
        Assertions.assertFalse(attackConfiguration.getAttackProperties().isEmpty());
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.CPU));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.MEMORY));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().anyMatch(each -> each.getType() == AttackType.LATENCY));
        Assertions.assertTrue(attackConfiguration.getAttackProperties().stream().noneMatch(each -> each.getType() == AttackType.EXCEPTION));
    }
}
