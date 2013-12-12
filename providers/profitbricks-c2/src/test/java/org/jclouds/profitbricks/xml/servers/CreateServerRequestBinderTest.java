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
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.replay;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.CreateServerRequestBinder}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "CreateServerRequestBinderTest")
public class CreateServerRequestBinderTest {

   @Test
   public void checkAllFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);
      expect(mapperMock.fromOSType(anyObject(OSType.class))).andReturn("OS_TYPE");
      expect(mapperMock.fromAvailabilityZone(anyObject(AvailabilityZone.class))).andReturn("ZONE");
      replay(mapperMock);

      CreateServerRequestBinder createServerRequestBinder = new CreateServerRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:createServer>" +
                                  "<request>" +
                                     "<dataCenterId>11111-2222-3333-4444-25195ac4515a</dataCenterId>" +
                                     "<serverName>LinuxServer</serverName>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<internetAccess>true</internetAccess>" +
                                     "<bootFromImageId>836acb2c-66f9-11e2-9478-0025901dfe2a</bootFromImageId>" +
                                     "<osType>OS_TYPE</osType>" +
                                     "<availabilityZone>ZONE</availabilityZone>" +
                                  "</request>" +
                               "</ws:createServer>";

      HttpRequest resultRequest = createServerRequestBinder.bindToRequest(request, createParams(maxInfoServerSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   @Test
   public void checkNotEmptyFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);

      expect(mapperMock.fromOSType(anyObject(OSType.class))).andReturn("");
      expect(mapperMock.fromAvailabilityZone(anyObject(AvailabilityZone.class))).andReturn("");
      replay(mapperMock);

      CreateServerRequestBinder createServerRequestBinder = new CreateServerRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:createServer>" +
                                  "<request>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<internetAccess>false</internetAccess>" +
                                  "</request>" +
                               "</ws:createServer>";

      HttpRequest resultRequest = createServerRequestBinder.bindToRequest(request, createParams(minInfoServerSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   private HttpRequest createRequest() {
      return HttpRequest.builder().method("POST").endpoint("http://home.local").build();
   }

   private HashMap<String,Object> createParams(ServerCreationSpec serverCreationSpec) {
      HashMap<String,Object> postParams = Maps.newHashMap();
      postParams.put(PBApiRequestParameters.SERVER_SPECIFICATION, serverCreationSpec);
      return postParams;
   }

   public ServerCreationSpec minInfoServerSpec() {
      return ServerCreationSpec.builder()
            .cores(2)
            .ram(1024)
            .build();
   }

   public ServerCreationSpec maxInfoServerSpec() {
      return ServerCreationSpec.builder()
            .dataCenterId("11111-2222-3333-4444-25195ac4515a")
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .osType(OSType.LINUX)
            .internetAccess(true)
            .bootFromImageId("836acb2c-66f9-11e2-9478-0025901dfe2a")
            .availabilityZone(AvailabilityZone.ZONE_1)
            .build();
   }

}
