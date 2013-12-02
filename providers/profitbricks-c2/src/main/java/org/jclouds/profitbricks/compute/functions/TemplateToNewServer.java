/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.profitbricks.compute.functions;

import com.google.common.base.Function;
import org.jclouds.compute.domain.Template;
import org.jclouds.profitbricks.domain.Server;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Function for transforming user general node Template to ProfitBricks
 * {@link org.jclouds.profitbricks.domain.Server} spec to create new virtual server.
 *
 * TODO test
 *
 * @author Serj Sintsov
 */
public class TemplateToNewServer implements Function<Template, Server> {

   @Override
   public Server apply(Template template) {
      checkNotNull(template, "template");

      return Server.creationBuilder()
                  // .dataCenterId(template.getLocation().getId()) // todo DC is an Optional param according to the API
                   .serverName(template.getHardware().getName())
                   .cores((int) template.getHardware().getProcessors().get(0).getCores())
                   .ram(template.getHardware().getRam())
                   .availabilityZone(Server.AvailabilityZone.ZONE_1)
                   .osType(Server.OSType.fromValue(template.getImage().getOperatingSystem().getFamily().name()))
                   .build();
   }

}
