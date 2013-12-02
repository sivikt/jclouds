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

import com.google.common.collect.Maps;
import org.jclouds.http.HttpRequest;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;
import org.jclouds.profitbricks.xml.servers.CreateServerRequestBinder;
import org.jclouds.profitbricks.xml.servers.ServerEnumsToStringMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.CreateServerRequestBinder}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "CreateServerRequestBinderTest")
public class CreateServerRequestBinderTest {

   private CreateServerRequestBinder createServerRequestBinder;
   private ServerEnumsToStringMapper mapperMock;

   @BeforeMethod
   public void setUp() {
      mapperMock = createMock(ServerEnumsToStringMapper.class);
      createServerRequestBinder = new CreateServerRequestBinder(mapperMock);
   }

   @Test
   public void checkAllFieldsAreMapped() {
      expect(mapperMock.mapOSType(anyObject(Server.OSType.class))).andStubReturn("OS_TYPE");
      expect(mapperMock.mapAvailabilityZone(anyObject(Server.AvailabilityZone.class))).andStubReturn("ZONE");
      replay(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:createServer>" +
                                  "<request>" +
                                     "<dataCenterId>11111-2222-3333-4444-25195ac4515a</dataCenterId>" +
                                     "<serverName>LinuxServer</serverName>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<internetAccess>true</internetAccess>" +
                                     "<osType>OS_TYPE</osType>" +
                                     "<availabilityZone>ZONE</availabilityZone>" +
                                  "</request>" +
                               "</ws:createServer>";

      HttpRequest resultRequest = createServerRequestBinder.bindToRequest(request, createParams(maxInfoServer()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   @Test
   public void checkNotEmptyFieldsAreMapped() {
      expect(mapperMock.mapOSType(anyObject(Server.OSType.class))).andStubReturn("");
      expect(mapperMock.mapAvailabilityZone(anyObject(Server.AvailabilityZone.class))).andStubReturn("");
      replay(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:createServer>" +
                                  "<request>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<internetAccess>false</internetAccess>" +
                                  "</request>" +
                               "</ws:createServer>";

      HttpRequest resultRequest = createServerRequestBinder.bindToRequest(request, createParams(minInfoServer()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   private HttpRequest createRequest() {
      return HttpRequest.builder().method("POST").endpoint("http://home.local").build();
   }

   private HashMap<String,Object> createParams(Server server) {
      HashMap<String,Object> postParams = Maps.newHashMap();
      postParams.put(PBApiRequestParameters.SERVER_ENTITY, server);
      return postParams;
   }

   public Server minInfoServer() {
      return Server.creationBuilder()
            .cores(2)
            .ram(1024)
            .build();
   }

   public Server maxInfoServer() {
      return Server.creationBuilder()
            .dataCenterId("11111-2222-3333-4444-25195ac4515a")
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .osType(Server.OSType.LINUX)
            .internetAccess(true)
            .availabilityZone(Server.AvailabilityZone.ZONE_1)
            .build();
   }

}
