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

import com.google.common.collect.ImmutableMap;
import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.Firewall;
import org.jclouds.profitbricks.domain.FirewallRule;
import org.jclouds.profitbricks.xml.BasePBResponseHandler;
import org.jclouds.profitbricks.xml.ResponseParamToEnumsMapper;
import org.xml.sax.Attributes;

import javax.inject.Inject;
import java.util.Map;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Parses XML response which fully describes {@link Firewall} entity.
 *
 * @author Serj Sintsov
 */
public abstract class BaseFullFirewallInfoResponseHandler<T> extends BasePBResponseHandler<T> {

   private ResponseParamToEnumsMapper paramToEnumsMapper;

   private enum ResponseGroup {
      FIREWALL,
      RULE
   }

   private Map<String, ResponseGroup> tagsToGroups = ImmutableMap.of(
      "return", ResponseGroup.FIREWALL,
      "firewallRules", ResponseGroup.RULE
   );

   private Stack<ResponseGroup> processingGroups;

   protected Firewall.Builder<?> fwBuilder;
   protected FirewallRule.Builder<?> ruleBuilder;
   protected Firewall currentFW;

   @Inject
   public BaseFullFirewallInfoResponseHandler(DateCodecFactory dateCodecFactory, ResponseParamToEnumsMapper paramToEnumsMapper) {
      super(dateCodecFactory);
      this.paramToEnumsMapper = checkNotNull(paramToEnumsMapper, "paramToEnumsMapper");
      processingGroups = new Stack<ResponseGroup>();
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) {
      ResponseGroup gr = tagsToGroups.get(qName);

      if (gr == null) return;

      switch (gr) {
         case RULE:
            ruleBuilder = FirewallRule.builder();
            break;
         case FIREWALL:
            fwBuilder = Firewall.builder();
            break;
      }

      processingGroups.push(gr);
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      ResponseGroup gr = tagsToGroups.get(qName);

      if (gr == null)
         setElementaryField(qName);
      else
         endGroupOfElementaryFields(gr);

      clearTextBuffer();
   }

   private void endGroupOfElementaryFields(ResponseGroup gr) {
      switch (gr) {
         case RULE:
            fwBuilder.addRule(ruleBuilder.build());
            processingGroups.pop();
            ruleBuilder = null;
            break;
         case FIREWALL:
            currentFW = fwBuilder.build();
            processingGroups.pop();
            break;
      }
   }

   private void setElementaryField(String qName) {
      if (processingGroups.empty())
         return;

      switch (processingGroups.peek()) {
         case RULE:
            setRuleInfoOnEndElementEvent(qName);
            break;
         case FIREWALL:
            setFirewallInfoOnEndElementEvent(qName);
            break;
      }
   }

   private void setRuleInfoOnEndElementEvent(String qName) {
      if (qName.equals("icmpCode")) ruleBuilder.icmpCode(textBufferToIntegerValue());
      else if (qName.equals("icmpType")) ruleBuilder.icmpType(textBufferToIntegerValue());
      else if (qName.equals("targetIp")) ruleBuilder.targetIp(trimAndGetTagStrValue());
      else if (qName.equals("protocol")) ruleBuilder.protocol(paramToEnumsMapper.toIpProtocol(trimAndGetTagStrValue()));
      else if (qName.equals("sourceIp")) ruleBuilder.sourceIp(trimAndGetTagStrValue());
      else if (qName.equals("sourceMac")) ruleBuilder.sourceMac(trimAndGetTagStrValue());
      else if (qName.equals("portRangeEnd")) ruleBuilder.toPort(textBufferToIntegerValue());
      else if (qName.equals("portRangeStart")) ruleBuilder.fromPort(textBufferToIntegerValue());
      else if (qName.equals("firewallRuleId")) ruleBuilder.id(trimAndGetTagStrValue());
   }

   private void setFirewallInfoOnEndElementEvent(String qName) {
      if (qName.equals("nicId")) fwBuilder.nicId(trimAndGetTagStrValue());
      else if (qName.equals("active")) fwBuilder.active(textBufferToBoolValue());
      else if (qName.equals("firewallId")) fwBuilder.id(trimAndGetTagStrValue());
      else if (qName.equals("provisioningState")) fwBuilder.provisioningState(paramToEnumsMapper.toProvState(trimAndGetTagStrValue()));
   }

}
