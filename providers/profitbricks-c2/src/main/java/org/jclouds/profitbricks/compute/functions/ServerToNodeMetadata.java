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
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.Server;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A function for transforming a ProfitBricks {@link Server} into a generic
 * {@link NodeMetadata} object.
 *
 * @author Serj Sintsov
 */
public class ServerToNodeMetadata implements Function<Server, NodeMetadata> {

   private Function<OSType, OperatingSystem> osTypeToOperatingSystem;
   private Function<Server, NodeMetadata.Status> serverStateToNodeStatus;

   @Inject
   public ServerToNodeMetadata(Function<OSType, OperatingSystem> osTypeToOperatingSystem,
                               Function<Server, NodeMetadata.Status> serverStateToNodeStatus) {
      this.osTypeToOperatingSystem = checkNotNull(osTypeToOperatingSystem, "osTypeToOperatingSystem");
      this.serverStateToNodeStatus = checkNotNull(serverStateToNodeStatus, "serverStateToNodeStatus");
   }

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
         .status(serverStateToNodeStatus.apply(server))
         .operatingSystem(osTypeToOperatingSystem.apply(server.getOsType()))
         .hardware(hardwareBuilder.build())
         .location(region);

      return nodeMetadataBuilder.build();
   }

}
