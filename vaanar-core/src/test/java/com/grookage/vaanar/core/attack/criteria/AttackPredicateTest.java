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

package com.grookage.vaanar.core.attack.criteria;

import com.grookage.vaanar.core.ResourceHelper;
import com.grookage.vaanar.core.attack.cpu.CPUAttackProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AttackPredicateTest {

    @Test
    @SneakyThrows
    void testAttackPredicate() {
        var properties = ResourceHelper.getResource(
                "cpuProperties.json",
                CPUAttackProperties.class
        );
        final var predicate = new AttackPredicate();
        Assertions.assertTrue(predicate.test(properties));
        properties.setInterceptable(true);
        Assertions.assertTrue(predicate.test(properties));
        properties = ResourceHelper.getResource(
                "customcpuProperties.json",
                CPUAttackProperties.class
        );
        Assertions.assertFalse(predicate.test(properties));
        properties.setInterceptable(true);
        Assertions.assertFalse(predicate.test(properties));
    }
}
