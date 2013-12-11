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

import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.domain.*;
import org.jclouds.profitbricks.domain.NIC;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.GetServerResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetServerResponseHandlerTest")
public class GetServerResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/servers/getServerResponse.xml");

      Server expectedServer = expectedServer();

      GetServerResponseHandler handler = injector.getInstance(GetServerResponseHandler.class);
      Server actualServer = factory.create(handler).parse(is);

      assertNotNull(actualServer);

      assertNotNull(actualServer);
      assertEquals(actualServer.getId(), expectedServer.getId());
      assertEquals(actualServer.getRam(), expectedServer.getRam());
      assertEquals(actualServer.getAvailabilityZone(), expectedServer.getAvailabilityZone());
      assertEquals(actualServer.getCores(), expectedServer.getCores());
      assertEquals(actualServer.getCreationTime(), expectedServer.getCreationTime());
      assertEquals(actualServer.getDataCenterId(), expectedServer.getDataCenterId());
      assertEquals(actualServer.getLastModificationTime(), expectedServer.getLastModificationTime());
      assertEquals(actualServer.getOsType(), expectedServer.getOsType());
      assertEquals(actualServer.getProvisioningState(), expectedServer.getProvisioningState());
      assertEquals(actualServer.getServerName(), expectedServer.getServerName());
      assertEquals(actualServer.getVirtualMachineState(), expectedServer.getVirtualMachineState());

      assertNotNull(actualServer.getNics());
      for (NIC expectedNic : expectedServer.getNics()) {
         NIC actualNic = findInSet(actualServer.getNics(), expectedNic.getId());

         assertNotNull(actualNic, errForNic(expectedNic));
         assertEquals(actualNic.getNicName(), expectedNic.getNicName());
         assertEquals(actualNic.getProvisioningState(), expectedNic.getProvisioningState());
         assertEquals(actualNic.getServerId(), expectedNic.getServerId());
         assertEquals(actualNic.getGatewayIp(), expectedNic.getGatewayIp());
         assertEquals(actualNic.getLanId(), expectedNic.getLanId());
         assertEquals(actualNic.getMacAddress(), expectedNic.getMacAddress());
         assertEquals(actualNic.isInternetAccess(), expectedNic.isInternetAccess());
         assertEquals(actualNic.isDhcpActive(), expectedNic.isDhcpActive());
         assertEquals(actualNic.getIps(), expectedNic.getIps());
      }
   }

   private String errForNic(NIC nic) {
      return "expected id=" + nic.getServerId();
   }

   private NIC findInSet(Set<NIC> src, String nicID) {
      for (NIC nic : src)
         if (nic.getId().equals(nicID)) return nic;

      return null;
   }

   private Server expectedServer() {
      SimpleDateFormatDateService dateService = new SimpleDateFormatDateService();

      return Server.builder()
                   .dataCenterId("95d08b87-36e5-47fd-9fb5-c244f566bc62")
                   .id("b804c14f-1d73-4204-a697-e1bd4ebd04c9")
                   .serverName("server")
                   .cores(3)
                   .ram(2048)
                   .creationTime(dateService.iso8601DateParse("2013-12-09T12:11:02.314Z"))
                   .lastModificationTime(dateService.iso8601DateParse("2013-12-09T13:07:33.008Z"))
                   .provisioningState(ProvisioningState.AVAILABLE)
                   .virtualMachineState(Server.VirtualMachineState.RUNNING)
                   .osType(OSType.LINUX)
                   .availabilityZone(AvailabilityZone.ZONE_2)
                   .addNIC(NIC.builder()
                         .id("f25fa8e0-d35c-4520-9ff0-1dc6adf1d9a7")
                         .serverId("b804c14f-1d73-4204-a697-e1bd4ebd04c9")
                         .addIP("78.137.99.213")
                         .macAddress("02:01:a4:af:c0:f8")
                         .internetAccess(true)
                         .dhcpActive(true)
                         .gatewayIp("78.137.99.1")
                         .lanId(1)
                         .provisioningState(ProvisioningState.AVAILABLE)
                         .build())
                   .addNIC(NIC.builder()
                         .nicName("MainMain")
                         .id("db37ecd8-daec-4b00-b629-e3e54d03ea13")
                         .serverId("b804c14f-1d73-4204-a697-e1bd4ebd04c9")
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

}
