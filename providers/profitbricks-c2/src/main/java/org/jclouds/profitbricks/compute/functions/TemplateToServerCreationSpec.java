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
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Function for transforming user general node {@link Template} to ProfitBricks
 * {@link org.jclouds.profitbricks.domain.Server} spec to create new virtual server.
 *
 * TODO test
 *
 * @author Serj Sintsov
 */
public class TemplateToServerCreationSpec implements Function<Template, ServerCreationSpec> {

   @Override
   public ServerCreationSpec apply(Template template) {
      checkNotNull(template, "template");

      return ServerCreationSpec.builder()
             .dataCenterId(chooseDC(template))
             .availabilityZone(chooseAvailabilityZone(template))
             .serverName(template.getHardware().getName())
             .cores((int) template.getHardware().getProcessors().get(0).getCores())
             .ram(template.getHardware().getRam())
             .bootFromImageId(template.getImage().getId())
             .osType(OSType.fromValue(template.getImage().getOperatingSystem().getFamily().name()))
             .build();
   }

   private String chooseDC(Template template) {
      Location dcLocation = findLocation(template.getLocation(), LocationScope.ZONE);
      if (dcLocation != null)
         return dcLocation.getId();

      return null;
   }

   private AvailabilityZone chooseAvailabilityZone(Template template) {
      if (template.getLocation().getScope() == LocationScope.HOST)
         return AvailabilityZone.fromValue(template.getLocation().getId());
      else
         return null;
   }

   private Location findLocation(Location location, LocationScope scope) {
      if (location.getScope() == scope) return location;
      else if (location.getParent() == null) return null;
      else return findLocation(location.getParent(), scope);
   }

}
