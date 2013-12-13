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
package org.jclouds.profitbricks.compute.options;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ProfitBricks' provider-specific {@link TemplateOptions}.
 * Use it to describe nodes you want to create more precisely.
 *
 * @author Serj Sintsov
 */
public class PBTemplateOptions extends TemplateOptions implements Cloneable {

   // TODO what's the purpose to duplicate mutation methods here?
   public static class Builder {

      /**
       * @see {@link PBTemplateOptions#getServerSpec()}
       */
      public PBTemplateOptions serverSpec(ServerCreationSpec serverSpec) {
         PBTemplateOptions options = new PBTemplateOptions();
         options.serverSpec(serverSpec);
         return options;
      }

      /**
       * @see {@link PBTemplateOptions#getFirewallRuleSpecs()}
       */
      public PBTemplateOptions firewallRuleSpecs(Set<FirewallRuleCreationSpec> firewallRuleSpecs) {
         PBTemplateOptions options = new PBTemplateOptions();
         options.firewallRuleSpecs(firewallRuleSpecs);
         return options;
      }

   }

   protected ServerCreationSpec serverSpec;
   protected Set<FirewallRuleCreationSpec> firewallRuleSpecs = ImmutableSet.of();

   /**
    * @see {@link PBTemplateOptions#getServerSpec()}
    */
   public PBTemplateOptions serverSpec(ServerCreationSpec serverSpec) {
      this.serverSpec = checkNotNull(serverSpec, "serverSpec");
      return this;
   }

   /**
    * @see {@link PBTemplateOptions#getFirewallRuleSpecs()}
    */
   public PBTemplateOptions addFirewallRuleSpec(FirewallRuleCreationSpec firewallRuleSpec) {
      checkNotNull(firewallRuleSpec, "firewallRuleSpec");
      Set<FirewallRuleCreationSpec> copy = Sets.newHashSet(firewallRuleSpecs);
      copy.add(firewallRuleSpec);
      firewallRuleSpecs = ImmutableSet.copyOf(copy);
      return this;
   }

   /**
    * @see {@link PBTemplateOptions#getFirewallRuleSpecs()}
    */
   public PBTemplateOptions firewallRuleSpecs(Set<FirewallRuleCreationSpec> firewallRuleSpecs) {
      this.firewallRuleSpecs = ImmutableSet.copyOf(checkNotNull(firewallRuleSpecs, "firewallRuleSpecs"));
      return this;
   }

   /**
    * @return The description of the server you want to create in terms of ProfitBricks.
    *         It's used to describe the node more precisely.
    */
   public ServerCreationSpec getServerSpec() {
      return serverSpec;
   }

   /**
    * @return Contains the set of firewall rules you want to apply to the node.
    */
   public Set<FirewallRuleCreationSpec> getFirewallRuleSpecs() {
      return firewallRuleSpecs;
   }

   @Override
   public PBTemplateOptions clone() {
      PBTemplateOptions options = new PBTemplateOptions();
      copyTo(options);
      return options;
   }

   @Override
   public void copyTo(TemplateOptions to) {
      super.copyTo(to);
      if (to instanceof PBTemplateOptions) {
         PBTemplateOptions eTo = PBTemplateOptions.class.cast(to);
         eTo.firewallRuleSpecs(getFirewallRuleSpecs());
         eTo.serverSpec(getServerSpec());
      }
   }

}
