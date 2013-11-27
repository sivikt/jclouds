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
package org.jclouds.profitbricks.functions;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.Server;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link ServerToNodeMetadata}
 *
 * @author Serj Sintsov
 */
public class ServerToNodeMetadataTest {

   @Test
   public void testApply() {
      ServerToNodeMetadata func = new ServerToNodeMetadata();
      Server actualServer = actualServer();
      NodeMetadata nodeMetadata = func.apply(actualServer);

      assertNotNull(nodeMetadata);
      assertEquals(nodeMetadata.getId(), actualServer.getDataCenterId() + "/" + actualServer.getServerId());
      assertEquals(nodeMetadata.getHostname(), actualServer.getServerName());
      assertEquals(nodeMetadata.getName(), actualServer.getServerName());
      assertEquals(nodeMetadata.getProviderId(), actualServer.getServerId());
      assertEquals(nodeMetadata.getStatus(), NodeMetadata.Status.RUNNING);

      assertNotNull(nodeMetadata.getOperatingSystem());
      assertEquals(nodeMetadata.getOperatingSystem().getFamily(), OsFamily.LINUX);
      assertEquals(nodeMetadata.getOperatingSystem().getDescription(), OsFamily.LINUX.name());

      assertNotNull(nodeMetadata.getLocation());
      assertEquals(nodeMetadata.getLocation().getId(), actualServer.getDataCenterId());
      assertEquals(nodeMetadata.getLocation().getScope(), LocationScope.REGION);
      assertEquals(nodeMetadata.getLocation().getDescription(), actualServer.getDataCenterId());

      assertNotNull(nodeMetadata.getHardware());
      assertNotNull(nodeMetadata.getHardware().getProcessors());
      assertEquals(nodeMetadata.getHardware().getProcessors().size(), 1);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getCores(), 2.0);
      assertEquals(nodeMetadata.getHardware().getProcessors().get(0).getSpeed(), 0.0);
      assertEquals(nodeMetadata.getHardware().getRam(), 1024);
      assertNotNull(nodeMetadata.getHardware().getLocation());
      assertEquals(nodeMetadata.getHardware().getLocation().getId(), Server.AvailabilityZone.ZONE_1.name());
      assertEquals(nodeMetadata.getHardware().getLocation().getDescription(), Server.AvailabilityZone.ZONE_1.name());
      assertEquals(nodeMetadata.getHardware().getLocation().getScope(), LocationScope.ZONE);
      assertEquals(nodeMetadata.getHardware().getLocation().getParent(), nodeMetadata.getLocation());
   }

   public Server actualServer() {
      return Server.describeInstance()
            .dataCenterId("11111-2222-3333-4444-25195ac4515a")
            .serverId("47491020-5c6a-1f75-1548-25195ac4515a")
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .creationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-26T06:21:13Z"))
            .lastModificationTime(new SimpleDateFormatDateService().iso8601SecondsDateParse("2013-11-27T21:43:15Z"))
            .provisioningState(Server.ProvisioningState.AVAILABLE)
            .virtualMachineState(Server.VirtualMachineState.RUNNING)
            .osType(Server.OSType.LINUX)
            .internetAccess(true)
            .availabilityZone(Server.AvailabilityZone.ZONE_1)
            .build();
   }

}
