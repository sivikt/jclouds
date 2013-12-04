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
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.Server;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A function for transforming a ProfitBricks {@link Server} into a generic
 * NodeMetadata object.
 *
 * @author Serj Sintsov
 */
public class ServerToNodeMetadata implements Function<Server, NodeMetadata> {

   @Override
   public NodeMetadata apply(Server server) {
      checkNotNull(server, "server");

      Location region = new LocationBuilder()
         .id(server.getDataCenterId())
         .description(server.getDataCenterId())
         .scope(LocationScope.ZONE)
         .build();

      /**
       * ProfitBricks locate servers in several availability zones
       * {@link org.jclouds.profitbricks.domain.Server.AvailabilityZone}.
       * For the moment we don't know iso codes for this zones.
       */
      Location serverZone = server.getAvailabilityZone().toLocation(region);

      HardwareBuilder hardwareBuilder = new HardwareBuilder()
         .id(server.getServerId())
         .processor(new Processor(server.getCores(), 0))
         .ram(server.getRam())
         .location(serverZone);

      NodeMetadataBuilder nodeMetadataBuilder = new NodeMetadataBuilder()
         .id(server.getServerId())
         .providerId(server.getServerId())
         .name(server.getServerName())
         .hostname(server.getServerName())
         .status(mapStatus(server.getVirtualMachineState()))
         .operatingSystem(mapOS(server.getOsType()))
         .hardware(hardwareBuilder.build())
         .location(region);

      return nodeMetadataBuilder.build();
   }

   // TODO move to configuration module
   protected OperatingSystem mapOS(Server.OSType osType) {
      if (osType == null)
         return OperatingSystem.builder()
                  .description(OsFamily.UNRECOGNIZED.value())
                  .family(OsFamily.UNRECOGNIZED)
                  .build();

      switch (osType) {
         case WINDOWS:
            return OperatingSystem.builder()
                     .description(OsFamily.WINDOWS.value())
                     .family(OsFamily.WINDOWS)
                     .build();
         case LINUX:
            return OperatingSystem.builder()
                     .description(OsFamily.LINUX.value())
                     .family(OsFamily.LINUX)
                     .build();
         default:
            return OperatingSystem.builder()
                     .description(OsFamily.UNRECOGNIZED.value())
                     .family(OsFamily.UNRECOGNIZED)
                     .build();
      }
   }

   // TODO move to configuration module
   protected NodeMetadata.Status mapStatus(Server.VirtualMachineState state) {
      if (state == null) return NodeMetadata.Status.UNRECOGNIZED;

      switch (state) {
         case SHUTDOWN:
         case SHUTOFF:
         case PAUSED:  return NodeMetadata.Status.SUSPENDED;
         case RUNNING: return NodeMetadata.Status.RUNNING;
         case BLOCKED: return NodeMetadata.Status.PENDING;
         case CRASHED: return NodeMetadata.Status.ERROR;
         default:      return NodeMetadata.Status.UNRECOGNIZED;
      }
   }

}
