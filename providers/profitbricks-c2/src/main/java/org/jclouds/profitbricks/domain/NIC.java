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
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.jclouds.javax.annotation.Nullable;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Class represents Network Interface Controller (NIC)
 *
 * @author Serj Sintsov
 */
public class NIC {

   public static Builder<?> builder() {
      return new NICDescribingBuilder();
   }

   public abstract static class Builder<T extends Builder<T>> {

      protected abstract T self();

      protected String id;
      protected String nicName;
      protected String serverId;
      protected int lanId;
      protected boolean internetAccess;
      protected Set<String> ips = Sets.newHashSet();
      protected String macAddress;
      protected boolean dhcpActive;
      protected String gatewayIp;
      protected ProvisioningState provisioningState;
      protected String firewallId;

      /**
       * @see NIC#getNicName()
       */
      public T nicName(String nicName) {
         this.nicName = nicName;
         return self();
      }

      /**
       * @see NIC#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

      /**
       * @see NIC#getServerId()
       */
      public T serverId(String serverId) {
         this.serverId = serverId;
         return self();
      }

      /**
       * @see NIC#getLanId()
       */
      public T lanId(int lanId) {
         this.lanId = lanId;
         return self();
      }

      /**
       * @see NIC#isInternetAccess()
       */
      public T internetAccess(boolean internetAccess) {
         this.internetAccess = internetAccess;
         return self();
      }

      /**
       * @see NIC#getIps()
       */
      public T addIP(String ip) {
         checkNotNull(ip, "ip");
         this.ips.add(ip);
         return self();
      }

      /**
       * @see NIC#getMacAddress()
       */
      public T macAddress(String macAddress) {
         this.macAddress = macAddress;
         return self();
      }

      /**
       * @see NIC#isDhcpActive()
       */
      public T dhcpActive(boolean dhcpActive) {
         this.dhcpActive = dhcpActive;
         return self();
      }

      /**
       * @see NIC#getGatewayIp()
       */
      public T gatewayIp(String gatewayIp) {
         this.gatewayIp = gatewayIp;
         return self();
      }

      /**
       * @see NIC#getProvisioningState()
       */
      public T provisioningState(ProvisioningState provisioningState) {
         this.provisioningState = provisioningState;
         return self();
      }

      /**
       * @see NIC#getProvisioningState()
       */
      public T provisioningState(String firewallId) {
         this.firewallId = firewallId;
         return self();
      }

      public NIC build() {
         checkNotNull(id, "id");
         nicName= Strings.emptyToNull(nicName);
         checkNotNull(serverId, "serverId");
         checkState(lanId > 0, "lanId <1");
         checkNotNull(ips, "ips");
         ips = ImmutableSet.copyOf(ips);
         checkNotNull(macAddress, "macAddress");
         checkNotNull(gatewayIp, "gatewayIp");
         checkNotNull(provisioningState, "provisioningState");

         return new NIC(id, nicName, serverId, lanId, internetAccess, ips, macAddress, dhcpActive, gatewayIp, firewallId,
                        provisioningState);
      }

   }

   public static class NICDescribingBuilder extends Builder<NICDescribingBuilder> {
      @Override
      protected NICDescribingBuilder self() {
         return this;
      }
   }

   private String id;
   private String nicName;
   private String serverId;
   private int lanId;
   private boolean internetAccess;
   private Set<String> ips;
   private String macAddress;
   private boolean dhcpActive;
   private String gatewayIp;
   private ProvisioningState provisioningState;
   private String firewallId;

   protected NIC(String id, @Nullable String nicName, String serverId, int lanId, boolean internetAccess,
                 Set<String> ips, String macAddress, boolean dhcpActive, String gatewayIp, String firewallId,
                 ProvisioningState provisioningState) {

      this.id = id;
      this.nicName = nicName;
      this.serverId = serverId;
      this.lanId = lanId;
      this.internetAccess = internetAccess;
      this.ips = ips;
      this.macAddress = macAddress;
      this.dhcpActive = dhcpActive;
      this.gatewayIp = gatewayIp;
      this.provisioningState = provisioningState;
      this.firewallId = firewallId;
   }

   /**
    * Identifier of the related firewall
    */
   @Nullable
   public String getFirewallId() {
      return firewallId;
   }

   @Nullable
   public String getNicName() {
      return nicName;
   }

   /**
    * Identifier of the virtual NIC
    */
   public String getId() {
      return id;
   }

   /**
    * Identifier of the target server
    */
   public String getServerId() {
      return serverId;
   }

   /**
    * Identifier of the target LAN
    */
   public int getLanId() {
      return lanId;
   }

   /**
    * Internet Access (TRUE/FALSE)
    */
   public boolean isInternetAccess() {
      return internetAccess;
   }

   /**
    * Lists all public and private IP addresses assigned to the NIC
    */
   public Set<String> getIps() {
      return ips;
   }

   public String getMacAddress() {
      return macAddress;
   }

   /**
    * Toggles usage of ProfitBricks DHCP
    */
   public boolean isDhcpActive() {
      return dhcpActive;
   }

   public String getGatewayIp() {
      return gatewayIp;
   }

   public ProvisioningState getProvisioningState() {
      return provisioningState;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      NIC that = NIC.class.cast(obj);
      return Objects.equal(this.id, that.id);
   }

   protected ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("id", id)
            .add("nicName", nicName)
            .add("serverId", serverId)
            .add("lanId", lanId)
            .add("internetAccess", internetAccess)
            .add("ips", ips)
            .add("macAddress", macAddress)
            .add("dhcpActive", dhcpActive)
            .add("gatewayIp", gatewayIp)
            .add("provisioningState", provisioningState)
            .add("firewallId", firewallId);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
