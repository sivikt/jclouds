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
import com.google.common.collect.Iterables;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;
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

   private JustProvider provider;
   private Function<OSType, OperatingSystem> osTypeToOperatingSystem;
   private Function<Server, NodeMetadata.Status> serverStateToNodeStatus;

   @Inject
   public ServerToNodeMetadata(JustProvider provider, Function<OSType, OperatingSystem> osTypeToOperatingSystem,
                               Function<Server, NodeMetadata.Status> serverStateToNodeStatus) {
      this.provider = checkNotNull(provider, "provider");
      this.osTypeToOperatingSystem = checkNotNull(osTypeToOperatingSystem, "osTypeToOperatingSystem");
      this.serverStateToNodeStatus = checkNotNull(serverStateToNodeStatus, "serverStateToNodeStatus");
   }

   @Override
   public NodeMetadata apply(Server server) {
      checkNotNull(server, "server");

      Location dc = new LocationBuilder()
         .id(server.getDataCenterId())
         .description("data_center")
         .scope(LocationScope.ZONE)
         .parent(Iterables.getOnlyElement(provider.get()))
         .build();

      /**
       * ProfitBricks locate servers in several availability zones
       * {@link org.jclouds.profitbricks.domain.Server.AvailabilityZone}.
       * For the moment we don't know iso codes for this zones.
       */
      Location serverAvailabilityZone = new LocationBuilder()
            .id(server.getAvailabilityZone().value())
            .description("availability_zone")
            .scope(LocationScope.ZONE)
            .parent(dc)
            .build();

      HardwareBuilder hardwareBuilder = new HardwareBuilder()
         .id(server.getId())
         .processor(new Processor(server.getCores(), 0))
         .ram(server.getRam())
         .location(serverAvailabilityZone);

      NodeMetadataBuilder nodeMetadataBuilder = new NodeMetadataBuilder()
         .id(server.getId())
         .providerId(server.getId())
         .name(server.getServerName())
         .hostname(server.getServerName())
         .status(serverStateToNodeStatus.apply(server))
         .operatingSystem(osTypeToOperatingSystem.apply(server.getOsType()))
         .hardware(hardwareBuilder.build())
         .publicAddresses(server.getIPs())
         .location(dc);

      return nodeMetadataBuilder.build();
   }

}
