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
package org.jclouds.profitbricks.xml.firewalls;

import com.google.common.collect.Maps;
import org.jclouds.http.HttpRequest;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.anyObject;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.CreateServerRequestBinder}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "CreateServerRequestBinderTest")
public class AddFirewallRuleRequestBinderTest {

   @Test
   public void checkAllFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);
      expect(mapperMock.mapIpProtocol(anyObject(IpProtocol.class))).andReturn("PROTOCOL_TYPE");
      replay(mapperMock);

      AddFirewallRuleRequestBinder requestBinder = new AddFirewallRuleRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:addFirewallRulesToNic>" +
                                 "<request>" +
                                    "<icmpCode>1</icmpCode>" +
                                    "<icmpType>2</icmpType>" +
                                    "<portRangeEnd>400</portRangeEnd>" +
                                    "<portRangeStart>1</portRangeStart>" +
                                    "<protocol>PROTOCOL_TYPE</protocol>" +
                                    "<sourceIp>1.2.3.4</sourceIp>" +
                                    "<sourceMac>aa:bb:cc:dd:ee:ff</sourceMac>" +
                                    "<targetIp>5.6.7.8</targetIp>" +
                                 "</request>" +
                                 "<nicId>ab-cd</nicId>" +
                               "</ws:addFirewallRulesToNic>";

      HttpRequest resultRequest = requestBinder.bindToRequest(request, createParams(maxInfoSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   @Test
   public void checkNotEmptyFieldsAreMapped() {
      EnumsToRequestParamMapper mapperMock = createMock(EnumsToRequestParamMapper.class);
      expect(mapperMock.mapIpProtocol(anyObject(IpProtocol.class))).andReturn(null);
      replay(mapperMock);

      AddFirewallRuleRequestBinder requestBinder = new AddFirewallRuleRequestBinder(mapperMock);

      HttpRequest request = createRequest();

      String expectedPayload = "<ws:addFirewallRulesToNic>" +
                                  "<request>" +
                                     "<protocol>null</protocol>" +
                                  "</request>" +
                                  "<nicId>ab-cd</nicId>" +
                               "</ws:addFirewallRulesToNic>";

      HttpRequest resultRequest = requestBinder.bindToRequest(request, createParams(minInfoSpec()));
      assertNotNull(resultRequest);
      assertEquals(resultRequest.getPayload().getRawContent(), expectedPayload);
   }

   private HttpRequest createRequest() {
      return HttpRequest.builder().method("POST").endpoint("http://home.local").build();
   }

   private HashMap<String,Object> createParams(FirewallRuleCreationSpec spec) {
      HashMap<String,Object> postParams = Maps.newHashMap();
      postParams.put(PBApiRequestParameters.FIREWALL_RULE_SPECIFICATION, spec);
      postParams.put(PBApiRequestParameters.NIC_ID, "ab-cd");
      return postParams;
   }

   public FirewallRuleCreationSpec minInfoSpec() {
      return FirewallRuleCreationSpec.builder()
            .protocol(IpProtocol.TCP)
            .build();
   }

   public FirewallRuleCreationSpec maxInfoSpec() {
      return FirewallRuleCreationSpec.builder()
            .fromPort(1)
            .toPort(400)
            .icmpCode(1)
            .icmpType(2)
            .protocol(IpProtocol.TCP)
            .sourceIp("1.2.3.4")
            .targetIp("5.6.7.8")
            .sourceMac("aa:bb:cc:dd:ee:ff")
            .build();
   }

}
