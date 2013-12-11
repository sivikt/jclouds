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
import com.google.common.collect.Sets;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.NIC;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.replay;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link ServerToNodeMetadata}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "ServerToNodeMetadataTest")
public class ServerToNodeMetadataTest {

   @Test
   @SuppressWarnings("unchecked")
   public void testApply() {
      OperatingSystem stubOs = OperatingSystem.builder()
            .description(OsFamily.LINUX.value())
            .family(OsFamily.LINUX)
            .build();

      Function<OSType, OperatingSystem> osTypeToOperatingSystemMock = createMock(Function.class);
      Function<Server, NodeMetadata.Status> serverStateToNodeStatusMock = createMock(Function.class);
      JustProvider provider = createMock(JustProvider.class);

      expect(osTypeToOperatingSystemMock.apply(anyObject(OSType.class))).andStubReturn(stubOs);
      expect(serverStateToNodeStatusMock.apply(anyObject(Server.class))).andStubReturn(NodeMetadata.Status.RUNNING);

      Location regionLoc = new LocationBuilder().id("region").description("reg").scope(LocationScope.REGION).build();
      Set locations = Sets.newHashSet(new LocationImpl());
      expect(provider.get()).andStubReturn(locations);

      replay(osTypeToOperatingSystemMock);
      replay(serverStateToNodeStatusMock);
      replay(provider);

      ServerToNodeMetadata func = new ServerToNodeMetadata(provider, osTypeToOperatingSystemMock, serverStateToNodeStatusMock);

      Server expectedServer = expectedServer();
      NodeMetadata nodeMetadata = func.apply(expectedServer);

      assertNotNull(nodeMetadata);
      assertEquals(nodeMetadata.getId(), expectedServer.getId());
      assertEquals(nodeMetadata.getHostname(), expectedServer.getServerName());
      assertEquals(nodeMetadata.getName(), expectedServer.getServerName());
      assertEquals(nodeMetadata.getProviderId(), expectedServer.getId());
      assertEquals(nodeMetadata.getStatus(), NodeMetadata.Status.RUNNING);

      assertNotNull(nodeMetadata.getOperatingSystem());
      assertEquals(nodeMetadata.getOperatingSystem().getFamily(), OsFamily.LINUX);
      assertEquals(nodeMetadata.getOperatingSystem().getDescription(), OsFamily.LINUX.value());

      assertNotNull(nodeMetadata.getLocation());
      assertEquals(nodeMetadata.getLocation().getId(), expectedServer.getDataCenterId());
      assertEquals(nodeMetadata.getLocation().getScope(), LocationScope.ZONE);
      assertEquals(nodeMetadata.getLocation().getDescription(), "data_center");
      assertEquals(nodeMetadata.getLocation().getParent().getScope(), regionLoc.getScope());
      assertEquals(nodeMetadata.getLocation().getParent().getId(), regionLoc.getId());

      assertNotNull(nodeMetadata.getHardware());
      assertEquals(nodeMetadata.getHardware().getId(), expectedServer.getId());
      assertNotNull(nodeMetadata.getHardware().getProcessors());
      assertEquals(nodeMetadata.getHardware().getProcessors().size(), 1);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getCores(), 2.0);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getSpeed(), 0.0);
      assertEquals(nodeMetadata.getHardware().getRam(), 1024);
      assertNotNull(nodeMetadata.getHardware().getLocation());
      assertEquals(nodeMetadata.getHardware().getLocation().getId(), AvailabilityZone.ZONE_1.value());
      assertEquals(nodeMetadata.getHardware().getLocation().getDescription(), "availability_zone");
      assertEquals(nodeMetadata.getHardware().getLocation().getScope(), LocationScope.ZONE);
      assertEquals(nodeMetadata.getHardware().getLocation().getParent(), nodeMetadata.getLocation());

      assertEquals(nodeMetadata.getPublicAddresses(), expectedServer.getIPs());
   }

   public Server expectedServer() {
      return Server.builder()
            .dataCenterId("11111-2222-3333-4444-25195ac4515a")
            .id("47491020-5c6a-1f75-1548-25195ac4515a")
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .creationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-26T06:21:13Z"))
            .lastModificationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-27T21:43:15Z"))
            .provisioningState(ProvisioningState.AVAILABLE)
            .virtualMachineState(Server.VirtualMachineState.RUNNING)
            .osType(OSType.LINUX)
            .availabilityZone(AvailabilityZone.ZONE_1)
            .addNIC(NIC.builder()
                  .nicName("MainMain")
                  .id("db37ecd8-daec-4b00-b629-e3e54d03ea13")
                  .serverId("47491020-5c6a-1f75-1548-25195ac4515a")
                  .addIP("46.16.77.120")
                  .addIP("46.16.79.250")
                  .addIP("46.16.79.249")
                  .macAddress("02:01:b2:6f:24:61")
                  .internetAccess(false)
                  .dhcpActive(false)
                  .lanId(1)
                  .gatewayIp("46.16.77.1")
                  .provisioningState(ProvisioningState.INPROCESS)
                  .build())
            .build();
   }

   private class LocationImpl implements Location {
      @Override
      public LocationScope getScope() {
         return LocationScope.REGION;
      }

      @Override
      public String getId() {
         return "region";
      }

      @Override
      public String getDescription() {
         return "reg";
      }

      @Override
      public Location getParent() {
         return null;
      }

      @Override
      public Map<String, Object> getMetadata() {
         return null;
      }

      @Override
      public Set<String> getIso3166Codes() {
         return null;
      }
   }

}
