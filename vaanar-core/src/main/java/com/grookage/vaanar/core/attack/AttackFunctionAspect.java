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

import com.grookage.vaanar.core.registry.AttackRegistryUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@SuppressWarnings("unused")
public class AttackFunctionAspect {

    private static final Logger log = LoggerFactory.getLogger(AttackFunctionAspect.class.getName());

    @Pointcut("@annotation(com.grookage.vaanar.core.attack.AttackFunction)")
    public void monitoredFunctionCalled() {
        //Empty as required
    }

    @Pointcut("execution(* *(..))")
    public void anyFunctionCalled() {
        //Empty as required
    }

    @Around("monitoredFunctionCalled() && anyFunctionCalled()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final var callSignature = joinPoint.getSignature();
        final var methodSignature = (MethodSignature) callSignature;
        final var attackFunction = methodSignature.getMethod().getAnnotation(AttackFunction.class);
        final var attackName = attackFunction.name();
        final var attacker = AttackRegistryUtils.getAttackRegistry().getAttacker(attackName).orElse(null);
        if (null != attacker && AttackRegistryUtils.getAttackRegistry().interpretableAttack(attackName)) {
            log.info("Discovered an attacker for attackName {}. Monkey enroute", attackName);
            attacker.attack();
        }
        return joinPoint.proceed();
    }
}
