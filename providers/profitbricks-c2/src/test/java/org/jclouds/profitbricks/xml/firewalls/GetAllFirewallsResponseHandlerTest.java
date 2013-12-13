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

import com.google.common.collect.Sets;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.Firewall;
import org.jclouds.profitbricks.domain.FirewallRule;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link GetAllFirewallsResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetAllFirewallsResponseHandlerTest")
public class GetAllFirewallsResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/firewalls/getAllFirewallsResponse.xml");

      Set<Firewall> expectedFWs = expectedFWs();

      GetAllFirewallsResponseHandler handler = injector.getInstance(GetAllFirewallsResponseHandler.class);
      Set<Firewall> actualFWs = factory.create(handler).parse(is);

      assertNotNull(actualFWs);

      assertNotNull(actualFWs);
      assertEquals(actualFWs.size(), 3);

      for (Firewall expectedFw : expectedFWs) {
         Firewall actualFw = findFWInSet(actualFWs, expectedFw.getId());
         assertNotNull(actualFw, errForFW(expectedFw));

         assertEquals(actualFw.getId(), expectedFw.getId());
         assertEquals(actualFw.getNicId(), expectedFw.getNicId());
         assertEquals(actualFw.getProvisioningState(), expectedFw.getProvisioningState());
         assertEquals(actualFw.isActive(), expectedFw.isActive());

         assertEquals(actualFw.getRules().size(), expectedFw.getRules().size());

         for (FirewallRule expectedRule : expectedFw.getRules()) {
            FirewallRule actualRule = findRuleInSet(actualFw.getRules(), expectedRule.getId());

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
   }

   private String errForRule(FirewallRule rule) {
      return "expected rule id=" + rule.getId();
   }

   private String errForFW(Firewall fw) {
      return "expected fw id=" + fw.getId();
   }

   private FirewallRule findRuleInSet(Set<FirewallRule> src, String ruleId) {
      for (FirewallRule rule : src)
         if (rule.getId().equals(ruleId)) return rule;

      return null;
   }

   private Firewall findFWInSet(Set<Firewall> src, String fwId) {
      for (Firewall fw : src)
         if (fw.getId().equals(fwId)) return fw;

      return null;
   }

   private Set<Firewall> expectedFWs() {
      return Sets.newHashSet(
            Firewall.builder()
                  .id("63dbd146-0db5-4874-ae27-57280f0e1967")
                  .nicId("66437ecb-6e9a-4579-9da8-7b87cc40273b")
                  .active(true)
                  .provisioningState(ProvisioningState.AVAILABLE)
                  .build(),
            Firewall.builder()
                  .id("dd2b6fa5-1d57-43a0-bd24-f12a1fab240e")
                  .nicId("a57145f1-ce7d-4fc8-9d7a-4fe47db60dfe")
                  .active(false)
                  .provisioningState(ProvisioningState.INACTIVE)
                  .addRule(FirewallRule.builder()
                        .id("9a75a472-0982-46bb-93aa-e046186849e8")
                        .icmpCode(1)
                        .icmpType(1)
                        .protocol(IpProtocol.ICMP)
                        .build())
                  .build(),
            Firewall.builder()
                  .id("dd2b6fa5-1d57-43a0-bd24-f12a1fab240e")
                  .nicId("a57145f1-ce7d-4fc8-9d7a-4fe47db60dfe")
                  .active(false)
                  .provisioningState(ProvisioningState.INACTIVE)
                  .addRule(FirewallRule.builder()
                        .id("ef7d4cff-2aa9-4093-bae1-8560b31b1752")
                        .fromPort(22)
                        .toPort(22)
                        .protocol(IpProtocol.TCP)
                        .targetIp("69.194.131.193")
                        .sourceIp("69.194.131.194")
                        .sourceMac("aa:dd:ff:hh:ee:aa")
                        .build())
                  .build()
      );
   }

}
