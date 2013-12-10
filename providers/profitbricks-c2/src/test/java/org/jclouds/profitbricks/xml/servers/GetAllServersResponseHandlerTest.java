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
package org.jclouds.profitbricks.xml.servers;

import com.google.common.collect.Lists;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.domain.*;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.GetAllServersResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetAllServersResponseHandlerTest")
public class GetAllServersResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/servers/getAllServersResponse.xml");

      List<Server> expectedResult = expectedResult();

      GetAllServersResponseHandler handler = injector.getInstance(GetAllServersResponseHandler.class);
      Set<Server> result = factory.create(handler).parse(is);

      assertNotNull(result);
      assertEquals(result.size(), 2);

      for (Server expectedServer : expectedResult) {
         Server actualServer = findInSet(result, expectedServer.getServerId());

         assertNotNull(actualServer, errForServer(expectedServer));
         assertEquals(actualServer.getRam(), expectedServer.getRam(), errForServer(expectedServer));
         assertEquals(actualServer.getAvailabilityZone(), expectedServer.getAvailabilityZone(), errForServer(expectedServer));
         assertEquals(actualServer.getCores(), expectedServer.getCores(), errForServer(expectedServer));
         assertEquals(actualServer.getCreationTime(), expectedServer.getCreationTime(), errForServer(expectedServer));
         assertEquals(actualServer.getDataCenterId(), expectedServer.getDataCenterId(), errForServer(expectedServer));
         assertEquals(actualServer.getLastModificationTime(), expectedServer.getLastModificationTime(), errForServer(expectedServer));
         assertEquals(actualServer.getOsType(), expectedServer.getOsType(), errForServer(expectedServer));
         assertEquals(actualServer.getProvisioningState(), expectedServer.getProvisioningState(), errForServer(expectedServer));
         assertEquals(actualServer.getServerName(), expectedServer.getServerName(), errForServer(expectedServer));
         assertEquals(actualServer.getVirtualMachineState(), expectedServer.getVirtualMachineState(), errForServer(expectedServer));
      }
   }

   private String errForServer(Server server) {
      return "expected serverId=" + server.getServerId();
   }

   private Server findInSet(Set<Server> src, String serverId) {
      for (Server server : src)
         if (server.getServerId().equals(serverId)) return server;

      return null;
   }

   private List<Server> expectedResult() {
      SimpleDateFormatDateService dateService = new SimpleDateFormatDateService();

      return Lists.newArrayList(
            Server.builder()
                  .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                  .serverId("fd4ffc52-1f2e-4a82-b155-75b2d5e6dd68")
                  .serverName("server")
                  .cores(2)
                  .ram(1024)
                  .creationTime(dateService.iso8601DateParse("2013-11-26T11:23:47.742Z"))
                  .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:23:47.742Z"))
                  .provisioningState(ProvisioningState.AVAILABLE)
                  .virtualMachineState(Server.VirtualMachineState.RUNNING)
                  .osType(OSType.LINUX)
                  .availabilityZone(AvailabilityZone.AUTO)
                  .addNIC(NIC.builder()
                        .nicId("f25fa8e0-d35c-4520-9ff0-1dc6adf1d9a7")
                        .serverId("fd4ffc52-1f2e-4a82-b155-75b2d5e6dd68")
                        .addIP("78.137.99.213")
                        .macAddress("02:01:a4:af:c0:f8")
                        .internetAccess(true)
                        .dhcpActive(true)
                        .gatewayIp("78.137.99.1")
                        .lanId(1)
                        .provisioningState(ProvisioningState.AVAILABLE)
                        .build())
                  .build(),

            Server.builder()
                  .dataCenterId("89046edb-2a50-4d0f-a153-6576ee7d22a7")
                  .serverId("722694b6-8635-4433-8dea-012860cab5fe")
                  .serverName("FirstServer")
                  .cores(1)
                  .ram(1024)
                  .creationTime(dateService.iso8601DateParse("2013-11-26T11:23:50.742Z"))
                  .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:23:50.742Z"))
                  .provisioningState(ProvisioningState.INACTIVE)
                  .virtualMachineState(Server.VirtualMachineState.PAUSED)
                  .osType(OSType.WINDOWS)
                  .availabilityZone(AvailabilityZone.ZONE_1)
                  .addNIC(NIC.builder()
                        .nicName("MainMain")
                        .nicId("db37ecd8-daec-4b00-b629-e3e54d03ea13")
                        .serverId("722694b6-8635-4433-8dea-012860cab5fe")
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
                  .addNIC(NIC.builder()
                        .nicId("f25fa8e0-d35c-4520-9ff0-1dc6adf1d9a7")
                        .serverId("722694b6-8635-4433-8dea-012860cab5fe")
                        .addIP("78.137.99.213")
                        .macAddress("02:01:a4:af:c0:f8")
                        .internetAccess(true)
                        .dhcpActive(true)
                        .gatewayIp("78.137.99.1")
                        .lanId(1)
                        .provisioningState(ProvisioningState.AVAILABLE)
                        .build())
                  .build()
      );
   }

}
