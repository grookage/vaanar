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

package com.grookage.vaanar.example;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.grookage.vaanar.core.VaanarEngine;
import com.grookage.vaanar.core.attack.AttackProperties;
import com.grookage.vaanar.core.attack.interceptor.AttackFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Getter
@Setter
@Path("/v1/monkey")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    @Inject
    private VaanarEngine vaanarEngine;

    @GET
    @Timed
    @ExceptionMetered
    @Path("/test")
    public List<AttackProperties> getAttackProperties() {
        return getProperties();
    }

    @AttackFunction(name = "latencyMonkey")
    public List<AttackProperties> getProperties() {
        return vaanarEngine.getAttackRegistry().getAttackProperties();
    }
}
