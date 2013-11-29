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
package org.jclouds.profitbricks.xml;

import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.domain.Server;
import org.testng.annotations.Test;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link GetServerResponseHandler}
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
      assertEquals(actualServer.getServerId(), expectedServer.getServerId());
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
      assertEquals(actualServer.isInternetAccess(), expectedServer.isInternetAccess());
   }

   private Server expectedServer() {
      SimpleDateFormatDateService dateService = new SimpleDateFormatDateService();

      return Server.describingBuilder()
                   .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                   .serverId("93981076-2511-4aa7-82c0-1e4df0d1737f")
                   .serverName("server")
                   .cores(2)
                   .ram(1024)
                   .creationTime(dateService.iso8601DateParse("2013-11-26T11:31:35.383Z"))
                   .lastModificationTime(dateService.iso8601DateParse("2013-11-26T11:31:35.383Z"))
                   .provisioningState(Server.ProvisioningState.AVAILABLE)
                   .virtualMachineState(Server.VirtualMachineState.RUNNING)
                   .osType(Server.OSType.WINDOWS)
                   .internetAccess(false)
                   .availabilityZone(Server.AvailabilityZone.AUTO)
                   .build();
   }

}
