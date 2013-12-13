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
package org.jclouds.profitbricks.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents Firewall entity which contains firewall rules applied to a certain NIC.
 * 
 * @author Serj Sintsov
 */
public class Firewall {

   public static FirewallDescribingBuilder builder() {
      return new FirewallDescribingBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected String id;
      protected String nicId;
      protected ProvisioningState provisioningState;
      protected boolean active;
      protected Set<FirewallRule> rules = Sets.newLinkedHashSet();
      
      protected abstract T self();

      public Firewall build() {
         checkFields();
         return buildInstance();
      }

      protected abstract Firewall buildInstance();

      /**
       * @see Firewall#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

      /**
       * @see Firewall#getNicId()
       */
      public T nicId(String nicId) {
         this.nicId = nicId;
         return self();
      }

      /**
       * @see Firewall#isActive()
       */
      public T active(boolean active) {
         this.active = active;
         return self();
      }

      /**
       * @see Firewall#getProvisioningState()
       */
      public T provisioningState(ProvisioningState provisioningState) {
         this.provisioningState = provisioningState;
         return self();
      }

      /**
       * @see Firewall#getRules()
       */
      public T addRule(FirewallRule rule) {
         checkNotNull(rule, "rule");
         rules.add(rule);
         return self();
      }

      protected void checkFields() {
         checkNotNull(id, "id");
         checkNotNull(nicId, "id");
         checkNotNull(provisioningState, "provisioningState");
         rules = ImmutableSet.copyOf(rules);
      }
   }

   public static class FirewallDescribingBuilder extends Builder<FirewallDescribingBuilder> {
      @Override
      protected FirewallDescribingBuilder self() {
         return this;
      }

      @Override
      protected Firewall buildInstance() {
         return new Firewall(id, nicId, active, provisioningState, rules);
      }
   }
   
   private String id;
   private String nicId;
   private ProvisioningState provisioningState;
   private boolean active;
   private Set<FirewallRule> rules;

   public Firewall(String id, String nicId, boolean active, ProvisioningState provisioningState, Set<FirewallRule> rules) {
      this.id = id;
      this.nicId = nicId;
      this.active = active;
      this.provisioningState = provisioningState;
      this.rules = rules;
   }

   /**
    * Identifier of the target {@link Firewall}
    */
   public String getId() {
      return id;
   }

   /**
    * Identifier of the target NIC
    */
   public String getNicId() {
      return nicId;
   }

   /**
    * Current provisioning state of the {@link Firewall}
    */
   public ProvisioningState getProvisioningState() {
      return provisioningState;
   }

   /**
    * {@code true} if {@link Firewall} is enabled and active
    */
   public boolean isActive() {
      return active;
   }

   /**
    * List all {@link Firewall} rules applied to this NIC
    */
   public Set<FirewallRule> getRules() {
      return rules;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id);
   }
   
   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Firewall that = Firewall.class.cast(o);
      return equal(this.id, that.id);
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("id", id)
            .add("nicId", nicId)
            .add("active", active)
            .add("provisioningState", provisioningState)
            .add("rules", rules);
   }
   
   @Override
   public String toString() {
      return string().toString();
   }

}
