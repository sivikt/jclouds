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
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.xml.servers.GetAllServersResponseHandler;
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
      assertEquals(result.size(), 3);

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
         assertEquals(actualServer.isInternetAccess(), expectedServer.isInternetAccess(), errForServer(expectedServer));
      }
   }

   private String errForServer(Server server) {
      return "serverId=" + server.getServerId();
   }

   private Server findInSet(Set<Server> src, String serverId) {
      for (Server server : src)
         if (server.getServerId().equals(serverId)) return server;

      return null;
   }

   private List<Server> expectedResult() {
      SimpleDateFormatDateService dateService = new SimpleDateFormatDateService();

      return Lists.newArrayList(
            Server.describingBuilder()
                  .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                  .serverId("fd4ffc52-1f2e-4a82-b155-75b2d5e6dd68")
                  .serverName("server")
                  .cores(2)
                  .ram(1024)
                  .creationTime(dateService.iso8601DateParse("2013-11-26T11:23:47.742Z"))
                  .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:23:47.742Z"))
                  .provisioningState(Server.ProvisioningState.AVAILABLE)
                  .virtualMachineState(Server.VirtualMachineState.RUNNING)
                  .osType(Server.OSType.LINUX)
                  .internetAccess(false)
                  .availabilityZone(Server.AvailabilityZone.AUTO)
                  .build(),

            Server.describingBuilder()
                  .dataCenterId("89046edb-2a50-4d0f-a153-6576ee7d22a7")
                  .serverId("722694b6-8635-4433-8dea-012860cab5fe")
                  .serverName("FirstServer")
                  .cores(1)
                  .ram(1024)
                  .creationTime(dateService.iso8601DateParse("2013-11-26T11:23:50.742Z"))
                  .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:23:50.742Z"))
                  .provisioningState(Server.ProvisioningState.INACTIVE)
                  .virtualMachineState(Server.VirtualMachineState.PAUSED)
                  .osType(Server.OSType.WINDOWS)
                  .internetAccess(true)
                  .availabilityZone(Server.AvailabilityZone.ZONE_1)
                  .build(),

            Server.describingBuilder()
                  .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                  .serverId("93981076-2511-4aa7-82c0-1e4df0d1737f")
                  .serverName("server")
                  .cores(2)
                  .ram(2048)
                  .creationTime(dateService.iso8601DateParse("2013-11-26T11:31:35.383Z"))
                  .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:31:35.383Z"))
                  .provisioningState(Server.ProvisioningState.DELETED)
                  .virtualMachineState(Server.VirtualMachineState.SHUTOFF)
                  .osType(Server.OSType.OTHER)
                  .internetAccess(false)
                  .availabilityZone(Server.AvailabilityZone.ZONE_2)
                  .build()
      );
   }

}
