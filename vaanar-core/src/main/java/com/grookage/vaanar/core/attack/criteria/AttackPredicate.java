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

import com.google.common.hash.Hashing;
import com.grookage.vaanar.core.attack.AttackProperties;

import java.util.UUID;
import java.util.function.Predicate;

public class AttackPredicate implements Predicate<AttackProperties> {

    /*
    There is no change in Hashing implementations from jre 23 to now. The marking of @Beta is to possibly include a further change.
    In all likelihood, it is possible they missed removing it. So using the suppressWarnings here!
    */
    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    @Override
    public boolean test(AttackProperties properties) {
        if (!properties.isEnabled()) return false;
        final var criteria = properties.getCriteria();
        if (null == criteria) return false;
        final var currentTime = System.currentTimeMillis();
        if (currentTime >= criteria.getStartAt() &&
                currentTime <= criteria.getEndAt()) {
            if (properties.isInterceptable()) {
                if (criteria.getMinAttackPercentage() == 0 &&
                        criteria.getMaxAttackPercentage() == 100)
                    return true;  //Do this check to not run bucketize for 100 percent envelopes.
                final var hashablePinningKey = Math.abs(Hashing.murmur3_32()
                        .newHasher()
                        .putBytes(UUID.randomUUID().toString().getBytes()).hash().asInt() % 10000);
                final var bucketId = hashablePinningKey / 100.0;
                return bucketId >= criteria.getMinAttackPercentage() &&
                        bucketId <= criteria.getMaxAttackPercentage();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}

