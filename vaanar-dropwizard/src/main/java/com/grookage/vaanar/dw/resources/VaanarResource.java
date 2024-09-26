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

package com.grookage.vaanar.dw.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.custom.CustomAttackerFactory;
import com.grookage.vaanar.core.registry.AttackRegistry;
import com.grookage.vaanar.core.registry.AttackRegistryUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Singleton
@Getter
@Setter
@Path("/v1/attacks")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class VaanarResource {

    private final AttackRegistry attackRegistry;
    private final CustomAttackerFactory attackerFactory;

    @GET
    @Timed
    @ExceptionMetered
    @Path("/details")
    private List<AttackProperties> getAttackProperties() {
        return attackRegistry.getAttackProperties();
    }

    @POST
    @Timed
    @ExceptionMetered
    @Path("/add")
    private Response addAttacker(AttackProperties attackProperty) {
        final var probableAttacker = AttackRegistryUtils.getAttacker(attackProperty, attackerFactory);
        probableAttacker.ifPresent(attacker -> attackRegistry.addAttacker(
                attackProperty.getName(),
                attacker
        ));
        return Response.ok().build();
    }

    @POST
    @Timed
    @ExceptionMetered
    @Path("/{attackName}/attack")
    private Response attack(@PathParam("attackName") @NotNull final String attackName) {
        final var attacker = attackRegistry.getAttacker(attackName).orElse(null);
        if (null == attacker) {
            return Response.status(400)
                    .entity("No Monkey Found for attack " + attackName + "Can't assault at this time.")
                    .build();
        }
        attacker.setupAttack();
        return Response.ok()
                .entity("Started the attack with attackName " + attackName)
                .build();
    }
}
