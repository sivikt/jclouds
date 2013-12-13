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
import org.jclouds.profitbricks.domain.specs.ServerUpdatingSpec;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link UpdateServerRequestBinder}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "UpdateServerRequestBinderTest")
public class UpdateServerRequestBinderTest {

   @Test
   public void checkAllFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);
      expect(mapperMock.fromOSType(anyObject(OSType.class))).andReturn("OS_TYPE");
      expect(mapperMock.fromAvailabilityZone(anyObject(AvailabilityZone.class))).andReturn("ZONE");
      replay(mapperMock);

      UpdateServerRequestBinder updateServerRequestBinder = new UpdateServerRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:updateServer>" +
                                  "<request>" +
                                     "<serverId>id1</serverId>" +
                                     "<serverName>LinuxServer</serverName>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<bootFromImageId>836acb2c-66f9-11e2-9478-0025901dfe2a</bootFromImageId>" +
                                     "<osType>OS_TYPE</osType>" +
                                     "<availabilityZone>ZONE</availabilityZone>" +
                                  "</request>" +
                               "</ws:updateServer>";

      HttpRequest resultRequest = updateServerRequestBinder.bindToRequest(request, createParams("id1", maxInfoServerSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   @Test
   public void checkNotEmptyFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);

      expect(mapperMock.fromOSType(anyObject(OSType.class))).andReturn("");
      expect(mapperMock.fromAvailabilityZone(anyObject(AvailabilityZone.class))).andReturn("");
      replay(mapperMock);

      UpdateServerRequestBinder updateServerRequestBinder = new UpdateServerRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:updateServer>" +
                                  "<request>" +
                                     "<serverId>id1</serverId>" +
                                     "<cores>2</cores>" +
                                     "<ram>1024</ram>" +
                                     "<bootFromStorageId>storageId</bootFromStorageId>" +
                                  "</request>" +
                               "</ws:updateServer>";

      HttpRequest resultRequest = updateServerRequestBinder.bindToRequest(request, createParams("id1", minInfoServerSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   private HttpRequest createRequest() {
      return HttpRequest.builder().method("POST").endpoint("http://home.local").build();
   }

   private HashMap<String,Object> createParams(String serverId, ServerUpdatingSpec serverUpdateSpec) {
      HashMap<String,Object> postParams = Maps.newHashMap();
      postParams.put(PBApiRequestParameters.SERVER_ID, serverId);
      postParams.put(PBApiRequestParameters.SERVER_SPECIFICATION, serverUpdateSpec);
      return postParams;
   }

   public ServerUpdatingSpec minInfoServerSpec() {
      return ServerUpdatingSpec.builder()
            .cores(2)
            .ram(1024)
            .bootFromStorageId("storageId")
            .build();
   }

   public ServerUpdatingSpec maxInfoServerSpec() {
      return ServerUpdatingSpec.builder()
            .serverName("LinuxServer")
            .cores(2)
            .ram(1024)
            .osType(OSType.LINUX)
            .bootFromImageId("836acb2c-66f9-11e2-9478-0025901dfe2a")
            .availabilityZone(AvailabilityZone.ZONE_1)
            .build();
   }

}
