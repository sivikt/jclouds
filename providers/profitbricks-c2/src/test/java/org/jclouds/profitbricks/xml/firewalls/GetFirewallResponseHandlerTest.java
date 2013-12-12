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

import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.*;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link GetFirewallResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetFirewallResponseHandlerTest")
public class GetFirewallResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/firewalls/getFirewallResponse.xml");

      Firewall expectedFW = expectedFW();

      GetFirewallResponseHandler handler = injector.getInstance(GetFirewallResponseHandler.class);
      Firewall actualFW = factory.create(handler).parse(is);

      assertNotNull(actualFW);

      assertNotNull(actualFW);
      assertEquals(actualFW.getId(), expectedFW.getId());
      assertEquals(actualFW.getNicId(), expectedFW.getNicId());
      assertEquals(actualFW.getProvisioningState(), expectedFW.getProvisioningState());
      assertEquals(actualFW.isActive(), expectedFW.isActive());

      assertNotNull(actualFW.getRules());
      assertEquals(actualFW.getRules().size(), 3);

      for (FirewallRule expectedRule : expectedFW.getRules()) {
         FirewallRule actualRule = findInSet(actualFW.getRules(), expectedRule.getId());

         assertNotNull(actualRule, errForRule(expectedRule));
         assertEquals(actualRule.getFromPort(), expectedRule.getFromPort());
         assertEquals(actualRule.getToPort(), expectedRule.getToPort());
         assertEquals(actualRule.getIcmpCode(), expectedRule.getIcmpCode());
         assertEquals(actualRule.getIcmpType(), expectedRule.getIcmpType());
         assertEquals(actualRule.getProtocol(), expectedRule.getProtocol());
         assertEquals(actualRule.getSourceIp(), expectedRule.getSourceIp());
         assertEquals(actualRule.getSourceMac(), expectedRule.getSourceMac());
         assertEquals(actualRule.getTargetIp(), expectedRule.getTargetIp());
      }
   }

   private String errForRule(FirewallRule rule) {
      return "expected id=" + rule.getId();
   }

   private FirewallRule findInSet(Set<FirewallRule> src, String ruleId) {
      for (FirewallRule rule : src)
         if (rule.getId().equals(ruleId)) return rule;

      return null;
   }

   private Firewall expectedFW() {
      return Firewall.builder()
                   .id("63dbd146-0db5-4874-ae27-57280f0e1967")
                   .nicId("66437ecb-6e9a-4579-9da8-7b87cc40273b")
                   .active(true)
                   .provisioningState(ProvisioningState.INPROCESS)
                   .addRule(FirewallRule.builder()
                         .id("f7747027-bdaf-4f01-a1ce-a2c86ab711bc")
                         .fromPort(22)
                         .toPort(22)
                         .protocol(IpProtocol.TCP)
                         .targetIp("69.194.130.64")
                         .build())
                   .addRule(FirewallRule.builder()
                         .id("11111-2222-3333-4444-55555")
                         .fromPort(1)
                         .toPort(400)
                         .icmpCode(254)
                         .icmpType(128)
                         .protocol(IpProtocol.TCP)
                         .targetIp("69.194.130.64")
                         .sourceIp("172.168.38.1")
                         .sourceMac("aa:bb:cc:dd:ee:ff")
                         .build())
                   .addRule(FirewallRule.builder()
                         .id("64b0d008-ff2a-43cd-b157-d0526ed5d8f8")
                         .protocol(IpProtocol.ALL)
                         .build())
                   .build();
   }

}
