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
import com.google.common.base.Strings;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.net.domain.IpProtocol;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a single Firewall Rule which allows traffic from
 * source to target dest.
 * 
 * @author Serj Sintsov
 */
public class FirewallRule {

   public static Builder<?> builder() {
      return new FirewallRuleDescribingBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected String id;
      protected IpProtocol protocol;
      protected String sourceMac;
      protected String sourceIp;
      protected String targetIp;
      protected Integer fromPort;
      protected Integer toPort;
      protected Integer icmpType;
      protected Integer icmpCode;

      protected abstract T self();

      public FirewallRule build() {
         checkFields();
         return buildInstance();
      }

      protected abstract FirewallRule buildInstance();

      /**
       * @see FirewallRule#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

      /**
       * @see FirewallRule#getProtocol()
       */
      public T protocol(IpProtocol protocol) {
         this.protocol = protocol;
         return self();
      }

      /**
       * @see FirewallRule#getSourceMac()
       */
      public T sourceMac(String sourceMac) {
         this.sourceMac = sourceMac;
         return self();
      }


      /**
       * @see FirewallRule#getSourceIp()
       */
      public T sourceIp(String sourceIp) {
         this.sourceIp = sourceIp;
         return self();
      }

      /**
       * @see FirewallRule#getTargetIp()
       */
      public T targetIp(String targetIp) {
         this.targetIp = targetIp;
         return self();
      }

      /**
       * @see FirewallRule#getFromPort()
       */
      public T fromPort(Integer fromPort) {
         this.fromPort = fromPort;
         return self();
      }

      /**
       * @see FirewallRule#getToPort()
       */
      public T toPort(Integer toPort) {
         this.toPort = toPort;
         return self();
      }

      /**
       * @see FirewallRule#getIcmpType()
       */
      public T icmpType(Integer icmpType) {
         this.icmpType = icmpType;
         return self();
      }

      /**
       * @see FirewallRule#getIcmpCode()
       */
      public T icmpCode(Integer icmpCode) {
         this.icmpCode = icmpCode;
         return self();
      }

      protected void checkFields() {
         checkNotNull(id, "id");
         checkNotNull(protocol, "protocol");
         sourceMac = Strings.emptyToNull(sourceMac);
         sourceIp = Strings.emptyToNull(sourceIp);
         targetIp = Strings.emptyToNull(targetIp);

         if (fromPort != null) {
            checkState(0 < fromPort && fromPort < 65535, "fromPort must be in range [1, 65534]");
            checkState(toPort != null, "toPort must be specified");
         }

         if (toPort != null) {
            checkState(0 < toPort && toPort < 65535, "toPort must be in range [1, 65534]");
            checkState(fromPort != null, "fromPort must be specified");
         }

         if (icmpCode != null)
            checkState(0 <= icmpCode && icmpCode < 255, "icmpCode must be in range [0, 254]");

         if (icmpType != null)
            checkState(0 <= icmpType && icmpType < 255, "icmpType must be in range [0, 254]");
      }
   }

   public static class FirewallRuleDescribingBuilder extends Builder<FirewallRuleDescribingBuilder> {
      @Override
      protected FirewallRuleDescribingBuilder self() {
         return this;
      }

      @Override
      protected FirewallRule buildInstance() {
         return new FirewallRule(id, protocol, sourceMac, sourceIp, targetIp, fromPort, toPort, icmpType, icmpCode);
      }
   }
   
   private String id;
   private IpProtocol protocol;
   private String sourceMac;
   private String sourceIp;
   private String targetIp;
   private Integer fromPort;
   private Integer toPort;
   private Integer icmpType;
   private Integer icmpCode;

   protected FirewallRule(String id, IpProtocol protocol, String sourceMac, String sourceIp, String targetIp, 
                          Integer fromPort, Integer toPort, Integer icmpType, Integer icmpCode) {
      this.id = id;
      this.protocol = protocol;
      this.sourceMac = sourceMac;
      this.sourceIp = sourceIp;
      this.targetIp = targetIp;
      this.fromPort = fromPort;
      this.toPort = toPort;
      this.icmpType = icmpType;
      this.icmpCode = icmpCode;
   }

   /**
    * Identifier (name) of the firewall rule.
    */
   public String getId() {
      return id;
   }

   /**
    * Allowed protocol.
    */
   public IpProtocol getProtocol() {
      return protocol;
   }

   /**
    * Only traffic originated from the respective MAC address is allowed.
    * If empty then it allows all source MAC address.
    */
   @Nullable
   public String getSourceMac() {
      return sourceMac;
   }

   /**
    * Only traffic originated from the respective IPv4 address is allowed.
    * If empty then it allows all source IPs.
    */
   @Nullable
   public String getSourceIp() {
      return sourceIp;
   }

   /**
    * In case the target NIC has multiple IP addresses, only traffic directed
    * to the respective IP address of the NIC is allowed. 
    * If empty then it allows all target IPs
    */
   @Nullable
   public String getTargetIp() {
      return targetIp;
   }

   /**
    * Defines the start range of the allowed port (from 1 to 65534) if protocol TCP or UDP is chosen.
    * If {@code getFromPort()} and {@link #getToPort()} are empty then it allows all ports.
    */
   @Nullable
   public Integer getFromPort() {
      return fromPort;
   }

   /**
    * Defines the start range of the allowed port (from 1 to 65534) if protocol TCP or UDP is chosen.
    * If {@link #getFromPort()} and {@code getToPort()} are empty then it allows all ports.
    */
   @Nullable
   public Integer getToPort() {
      return toPort;
   }

   /**
    * Defines the allowed type (from 0 to 254) if protocol ICMP is chosen.
    * If empty then allows all types.
    */
   @Nullable
   public Integer getIcmpType() {
      return icmpType;
   }

   /**
    * Defines the allowed code (from 0 to 254) if protocol ICMP is chosen.
    * If empty then allows all types.
    */
   @Nullable
   public Integer getIcmpCode() {
      return icmpCode;
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
      FirewallRule that = FirewallRule.class.cast(o);
      return equal(this.id, that.id);
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("id", id)
            .add("protocol", protocol)
            .add("sourceMac", sourceMac)
            .add("sourceIp", sourceIp)
            .add("targetIp", targetIp)
            .add("fromPort", fromPort)
            .add("toPort", toPort)
            .add("icmpType", icmpType)
            .add("icmpCode", icmpCode);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
