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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.Attacker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class AttackRegistry {

    private Map<String, Attacker> registry = Maps.newConcurrentMap();

    public void addAttacker(final String name, final Attacker attacker) {
        Preconditions.checkNotNull(name, "Name can't be null");
        Preconditions.checkNotNull(attacker, "Attacker can't be null");

        registry.putIfAbsent(name.toUpperCase(Locale.ROOT), attacker);
    }

    public Optional<Attacker> getAttacker(final String name) {
        Preconditions.checkNotNull(name, "Name can't be null");
        return Optional.ofNullable(registry.get(name.toUpperCase(Locale.ROOT)));
    }

    public List<AttackProperties> getAttackProperties() {
        return registry.values().stream().map(Attacker::getAttackProperties).toList();
    }

    public Collection<Attacker> attackers() {
        return registry.values();
    }

}
