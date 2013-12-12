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

import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.profitbricks.domain.Firewall;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.filters.PBSoapMessageEnvelope;
import org.jclouds.profitbricks.xml.firewalls.AddFirewallRuleRequestBinder;
import org.jclouds.profitbricks.xml.firewalls.GetFirewallResponseHandler;
import org.jclouds.rest.annotations.SinceApiVersion;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.Payload;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.XMLResponseParser;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static org.jclouds.profitbricks.xml.PBApiRequestParameters.FIREWALL_RULE_SPECIFICATION;
import static org.jclouds.profitbricks.xml.PBApiRequestParameters.NIC_ID;

/**
 * Provides synchronous access to ProfitBricks's Firewall Operations API.
 *
 * @author Serj Sintsov
 */
@SinceApiVersion("1.2")
@RequestFilters({BasicAuthentication.class, PBSoapMessageEnvelope.class})
@VirtualHost
public interface FirewallApi {

   /**
    * Adds accept-rule to the firewall of a given NIC.
    * @param nicId {@link org.jclouds.profitbricks.domain.NIC} identifier
    * @param ruleSpec {@link org.jclouds.profitbricks.domain.FirewallRule} specification
    */
   @POST // TODO live and expect test
   @Named("AddFirewallRule")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @MapBinder(AddFirewallRuleRequestBinder.class) // TODO add Fallback?
   void addFirewallRule(@PayloadParam(NIC_ID) String nicId,
                        @PayloadParam(FIREWALL_RULE_SPECIFICATION) FirewallRuleCreationSpec ruleSpec);

   /**
    * Adds accept-rule to the firewall of a given NIC.
    * @param firewallId {@link Firewall} identifier
    * @return an existing {@link Firewall} or {@code null}
    */
   @POST // TODO live and expect test
   @Named("GetFirewall")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @Payload("<ws:getFirewall><firewallId>{id}</firewallId></ws:getFirewall>")
   @XMLResponseParser(GetFirewallResponseHandler.class) // TODO add Fallback?
   Firewall getFirewall(@PayloadParam("id") String firewallId);

}
