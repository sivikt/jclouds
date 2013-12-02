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

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.BLOCKED;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.CRASHED;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.RUNNING;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.PAUSED;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.SHUTDOWN;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.SHUTOFF;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.NOSTATE;
import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.UNRECOGNIZED;
import org.testng.annotations.Test;

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
   public void testApply() {
      ServerToNodeMetadata func = new ServerToNodeMetadata();
      Server actualServer = actualServer();
      NodeMetadata nodeMetadata = func.apply(actualServer);

      assertNotNull(nodeMetadata);
      assertEquals(nodeMetadata.getId(), actualServer.getServerId());
      assertEquals(nodeMetadata.getHostname(), actualServer.getServerName());
      assertEquals(nodeMetadata.getName(), actualServer.getServerName());
      assertEquals(nodeMetadata.getProviderId(), actualServer.getServerId());
      assertEquals(nodeMetadata.getStatus(), NodeMetadata.Status.RUNNING);

      assertNotNull(nodeMetadata.getOperatingSystem());
      assertEquals(nodeMetadata.getOperatingSystem().getFamily(), OsFamily.LINUX);
      assertEquals(nodeMetadata.getOperatingSystem().getDescription(), OsFamily.LINUX.value());

      assertNotNull(nodeMetadata.getLocation());
      assertEquals(nodeMetadata.getLocation().getId(), actualServer.getDataCenterId());
      assertEquals(nodeMetadata.getLocation().getScope(), LocationScope.REGION);
      assertEquals(nodeMetadata.getLocation().getDescription(), actualServer.getDataCenterId());

      assertNotNull(nodeMetadata.getHardware());
      assertEquals(nodeMetadata.getHardware().getId(), actualServer.getServerId());
      assertNotNull(nodeMetadata.getHardware().getProcessors());
      assertEquals(nodeMetadata.getHardware().getProcessors().size(), 1);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getCores(), 2.0);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getSpeed(), 0.0);
      assertEquals(nodeMetadata.getHardware().getRam(), 1024);
      assertNotNull(nodeMetadata.getHardware().getLocation());
      assertEquals(nodeMetadata.getHardware().getLocation().getId(), Server.AvailabilityZone.ZONE_1.value());
      assertEquals(nodeMetadata.getHardware().getLocation().getDescription(), Server.AvailabilityZone.ZONE_1.value());
      assertEquals(nodeMetadata.getHardware().getLocation().getScope(), LocationScope.ZONE);
      assertEquals(nodeMetadata.getHardware().getLocation().getParent(), nodeMetadata.getLocation());
   }

   public Server actualServer() {
      return Server.describingBuilder()
            .dataCenterId("11111-2222-3333-4444-25195ac4515a")
            .serverId("47491020-5c6a-1f75-1548-25195ac4515a")
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .creationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-26T06:21:13Z"))
            .lastModificationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-27T21:43:15Z"))
            .provisioningState(ProvisioningState.AVAILABLE)
            .virtualMachineState(Server.VirtualMachineState.RUNNING)
            .osType(Server.OSType.LINUX)
            .internetAccess(true)
            .availabilityZone(Server.AvailabilityZone.ZONE_1)
            .build();
   }

   @Test
   public void testMapOS() {
      ServerToNodeMetadata func = new ServerToNodeMetadata();
      assertEquals(func.mapOS(Server.OSType.LINUX).getFamily(), OsFamily.LINUX);
      assertEquals(func.mapOS(Server.OSType.LINUX).getDescription(), OsFamily.LINUX.value());

      assertEquals(func.mapOS(Server.OSType.WINDOWS).getFamily(), OsFamily.WINDOWS);
      assertEquals(func.mapOS(Server.OSType.WINDOWS).getDescription(), OsFamily.WINDOWS.value());

      assertEquals(func.mapOS(Server.OSType.OTHER).getFamily(), OsFamily.UNRECOGNIZED);
      assertEquals(func.mapOS(Server.OSType.OTHER).getDescription(), OsFamily.UNRECOGNIZED.value());

      assertEquals(func.mapOS(Server.OSType.UNKNOWN).getFamily(), OsFamily.UNRECOGNIZED);
      assertEquals(func.mapOS(Server.OSType.UNKNOWN).getDescription(), OsFamily.UNRECOGNIZED.value());

      assertEquals(func.mapOS(null).getFamily(), OsFamily.UNRECOGNIZED);
      assertEquals(func.mapOS(null).getDescription(), OsFamily.UNRECOGNIZED.value());
   }

   @Test
   public void testMapStatus() {
      ServerToNodeMetadata func = new ServerToNodeMetadata();
      assertEquals(func.mapStatus(BLOCKED), NodeMetadata.Status.PENDING);
      assertEquals(func.mapStatus(CRASHED), NodeMetadata.Status.ERROR);
      assertEquals(func.mapStatus(NOSTATE), NodeMetadata.Status.UNRECOGNIZED);
      assertEquals(func.mapStatus(PAUSED), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.mapStatus(RUNNING), NodeMetadata.Status.RUNNING);
      assertEquals(func.mapStatus(SHUTDOWN), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.mapStatus(SHUTOFF), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.mapStatus(UNRECOGNIZED), NodeMetadata.Status.UNRECOGNIZED);
      assertEquals(func.mapStatus(null), NodeMetadata.Status.UNRECOGNIZED);
   }

}
