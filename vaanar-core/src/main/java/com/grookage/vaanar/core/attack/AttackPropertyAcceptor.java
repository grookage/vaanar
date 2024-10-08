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

import com.grookage.vaanar.core.attack.cpu.CPUAttackProperties;
import com.grookage.vaanar.core.attack.custom.CustomAttackProperties;
import com.grookage.vaanar.core.attack.exception.ExceptionAttackProperties;
import com.grookage.vaanar.core.attack.latency.LatencyAttackProperties;
import com.grookage.vaanar.core.attack.memory.MemoryAttackProperties;
import com.grookage.vaanar.core.attack.sigterm.SigtermAttackProperties;

public interface AttackPropertyAcceptor<T> {

    T accept(CPUAttackProperties properties);

    T accept(CustomAttackProperties properties);

    T accept(ExceptionAttackProperties properties);

    T accept(LatencyAttackProperties properties);

    T accept(MemoryAttackProperties properties);

    T accept(SigtermAttackProperties properties);
}
