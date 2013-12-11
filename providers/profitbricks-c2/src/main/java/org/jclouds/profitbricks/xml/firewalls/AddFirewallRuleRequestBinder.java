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

import org.jclouds.http.HttpRequest;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.xml.BaseRequestBinder;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;

import javax.inject.Inject;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create XML payload to send new server creation request.
 *
 * @author Serj Sintsov
 */
public class AddFirewallRuleRequestBinder extends BaseRequestBinder {

   private EnumsToRequestParamMapper enumsToRequestParam;

   @Inject
   public AddFirewallRuleRequestBinder(EnumsToRequestParamMapper enumsToRequestParam) {
      this.enumsToRequestParam = checkNotNull(enumsToRequestParam, "enumsToRequestParam");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkNotNull(request, "request");

      Object firewallRuleSpecObj = postParams.get(PBApiRequestParameters.FIREWALL_RULE_SPECIFICATION);
      Object nicIdObj = postParams.get(PBApiRequestParameters.NIC_ID);

      checkNotNull(firewallRuleSpecObj, "firewall rule specification");
      FirewallRuleCreationSpec firewallRuleSpec = FirewallRuleCreationSpec.class.cast(firewallRuleSpecObj);

      checkNotNull(nicIdObj, "nicId");
      String nicId = String.class.cast(nicIdObj);

      return createRequest(request, mapToXml(nicId, firewallRuleSpec));
   }

   private String mapToXml(String nicId, FirewallRuleCreationSpec spec) {
      return "<ws:createServer>" +
                "<request>" +
                   addIfNotNull("<icmpCode>%s</icmpCode>", spec.getIcmpCode()) +
                   addIfNotNull("<icmpType>%s</icmpType>", spec.getIcmpType()) +
                   addIfNotNull("<portRangeEnd>%s</portRangeEnd>", spec.getToPort()) +
                   addIfNotNull("<portRangeStart>%s</portRangeStart>", spec.getFromPort()) +
                   justAdd("<protocol>%s</protocol>", enumsToRequestParam.mapIpProtocol(spec.getProtocol())) +
                   addIfNotEmpty("<sourceIp>%s</sourceIp>", spec.getSourceIp()) +
                   addIfNotEmpty("<sourceMac>%s</sourceMac>", spec.getSourceMac()) +
                   addIfNotEmpty("<targetIp>%s</targetIp>", spec.getTargetIp()) +
                "</request>" +
                justAdd("<nicId>%s</nicId>", nicId) +
             "</ws:createServer>";
   }

}
