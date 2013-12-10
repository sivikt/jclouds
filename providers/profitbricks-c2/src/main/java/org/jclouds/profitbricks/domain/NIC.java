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

      protected String nicName;
      protected String nicId;
      protected String serverId;
      protected int lanId;
      protected boolean internetAccess;
      protected Set<String> ips = Sets.newHashSet();
      protected String macAddress;
      protected boolean dhcpActive;
      protected String gatewayIp;
      protected ProvisioningState provisioningState;

      /**
       * @see NIC#getNicName()
       */
      public T nicName(String nicName) {
         this.nicName = nicName;
         return self();
      }

      /**
       * @see NIC#getNicId()
       */
      public T nicId(String nicId) {
         this.nicId = nicId;
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

      public NIC build() {
         checkNotNull(nicId, "nicId");
         nicName= Strings.emptyToNull(nicName);
         checkNotNull(serverId, "serverId");
         checkState(lanId > 0, "lanId <1");
         checkNotNull(ips, "ips");
         ips = ImmutableSet.copyOf(ips);
         checkNotNull(macAddress, "macAddress");
         checkNotNull(gatewayIp, "gatewayIp");
         checkNotNull(provisioningState, "provisioningState");

         return new NIC(nicId, nicName, serverId, lanId, internetAccess, ips, macAddress, dhcpActive, gatewayIp,
                        provisioningState);
      }

   }

   public static class NICDescribingBuilder extends Builder<NICDescribingBuilder> {
      @Override
      protected NICDescribingBuilder self() {
         return this;
      }
   }

   private String nicName;
   private String nicId;
   private String serverId;
   private int lanId;
   private boolean internetAccess;
   private Set<String> ips;
   private String macAddress;
   private boolean dhcpActive;
   private String gatewayIp;
   private ProvisioningState provisioningState;

   protected NIC(String nicId, @Nullable String nicName, String serverId, int lanId, boolean internetAccess,
                 Set<String> ips, String macAddress, boolean dhcpActive, String gatewayIp,
                 ProvisioningState provisioningState) {

      this.nicId = nicId;
      this.nicName = nicName;
      this.serverId = serverId;
      this.lanId = lanId;
      this.internetAccess = internetAccess;
      this.ips = ips;
      this.macAddress = macAddress;
      this.dhcpActive = dhcpActive;
      this.gatewayIp = gatewayIp;
      this.provisioningState = provisioningState;
   }

   public String getNicName() {
      return nicName;
   }

   /**
    * Identifier of the virtual NIC
    */
   public String getNicId() {
      return nicId;
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
      return Objects.hashCode(nicId);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      NIC that = NIC.class.cast(obj);
      return Objects.equal(this.nicId, that.nicId);
   }

   protected ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("nicId", nicId)
            .add("nicName", nicName)
            .add("serverId", serverId)
            .add("lanId", lanId)
            .add("internetAccess", internetAccess)
            .add("ips", ips)
            .add("macAddress", macAddress)
            .add("dhcpActive", dhcpActive)
            .add("gatewayIp", gatewayIp)
            .add("provisioningState", provisioningState);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
