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
package org.jclouds.profitbricks.features;

import com.google.common.collect.ImmutableList;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.xml.firewalls.GetAllFirewallsResponseHandler;
import org.jclouds.profitbricks.xml.firewalls.GetFirewallResponseHandler;
import org.jclouds.reflect.Invocation;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.jclouds.reflect.Reflection2.method;
import static org.testng.Assert.assertNotNull;

/**
 * Integration test for {@link FirewallApi}.
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "FirewallApiTest")
public class FirewallApiTest extends BasePBApiTest<ServerApi> {

   @Test
   public void getFirewall() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
         method(FirewallApi.class, "getFirewall", String.class),
         ImmutableList.<Object>of("93981076-2511-4aa7-82c0-1e4df0d1737f")
      );

      GeneratedHttpRequest request = processor.apply(invocation);
      assertNotNull(request);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getFirewall><firewallId>93981076-2511-4aa7-82c0-1e4df0d1737f</firewallId></ws:getFirewall>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetFirewallResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void addFirewallRule() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
            method(FirewallApi.class, "addFirewallRule", String.class, FirewallRuleCreationSpec.class),
            ImmutableList.<Object>of(
                  "nicId-nicId-nicId-nicId",
                  FirewallRuleCreationSpec.builder()
                        .sourceIp("sourceIp")
                        .targetIp("targetIp")
                        .sourceMac("sourceMac")
                        .fromPort(1)
                        .toPort(40)
                        .icmpCode(254)
                        .icmpType(128)
                        .protocol(IpProtocol.TCP)
                        .build()
            )
      );

      String expectedPayload = "<ws:addFirewallRulesToNic>" +
                                  "<request>" +
                                     "<icmpCode>254</icmpCode>" +
                                     "<icmpType>128</icmpType>" +
                                     "<portRangeEnd>40</portRangeEnd>" +
                                     "<portRangeStart>1</portRangeStart>" +
                                     "<protocol>TCP</protocol>" +
                                     "<sourceIp>sourceIp</sourceIp>" +
                                     "<sourceMac>sourceMac</sourceMac>" +
                                     "<targetIp>targetIp</targetIp>" +
                                  "</request>" +
                                  "<nicId>nicId-nicId-nicId-nicId</nicId>" +
                               "</ws:addFirewallRulesToNic>";

      GeneratedHttpRequest request = processor.apply(invocation);
      assertNotNull(request);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, expectedPayload, "text/xml", false);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), null);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void getAllFirewalls() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(method(FirewallApi.class, "listFirewalls"));

      GeneratedHttpRequest request = processor.apply(invocation);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getAllFirewalls/>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetAllFirewallsResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void removeFirewallRule() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
            method(FirewallApi.class, "removeFirewallRule", String.class),
            ImmutableList.<Object>of("82c0")
      );

      GeneratedHttpRequest request = processor.apply(invocation);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:removeFirewallRules><firewallRuleIds>82c0</firewallRuleIds></ws:removeFirewallRules>", "text/xml", false);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), null);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void activate() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
            method(FirewallApi.class, "activateFirewall", String.class),
            ImmutableList.<Object>of("82c0")
      );

      GeneratedHttpRequest request = processor.apply(invocation);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:activateFirewalls><firewallIds>82c0</firewallIds></ws:activateFirewalls>", "text/xml", false);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), null);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void deactivate() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
            method(FirewallApi.class, "deactivateFirewall", String.class),
            ImmutableList.<Object>of("82c0")
      );

      GeneratedHttpRequest request = processor.apply(invocation);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:deactivateFirewalls><firewallIds>82c0</firewallIds></ws:deactivateFirewalls>", "text/xml", false);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), null);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

}
