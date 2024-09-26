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

package com.grookage.vaanar.core.attack.exception;

import com.grookage.vaanar.core.attack.AbstractAttacker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ExceptionAttacker extends AbstractAttacker {

    private final ExceptionAttackProperties attackProperties;

    public ExceptionAttacker(final ExceptionAttackProperties attackProperties) {
        this.attackProperties = attackProperties;
    }

    @Override
    @SneakyThrows
    public void attack() {
        log.info("Vaanar : Exception Attack Starting");
        throw new IllegalAccessException("Monkey see, monkey throw - RuntimeException");
    }
}
