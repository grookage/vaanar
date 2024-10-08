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

package com.grookage.vaanar.dw.interceptors;

import com.grookage.vaanar.core.attack.AttackExecutor;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.criteria.AttackPredicate;
import com.grookage.vaanar.core.attack.interceptor.AttackFunction;
import com.grookage.vaanar.core.registry.AttackRegistry;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class AttackFunctionInterceptor implements MethodInterceptor {

    private final Supplier<AttackRegistry> registrySupplier;
    private final Predicate<AttackProperties> criteria;

    @Override
    @SneakyThrows
    public Object invoke(MethodInvocation methodInvocation) {
        final var attackName = methodInvocation.getMethod().getAnnotation(AttackFunction.class);
        if (null != attackName) {
            final var attacker = registrySupplier.get().getAttacker(attackName.name()).orElse(null);
            if (null != attacker && criteria.test(attacker.getAttackProperties())) {
                attacker.setupAttack();
            }
        }
        return methodInvocation.proceed();
    }
}
