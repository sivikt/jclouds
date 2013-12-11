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
package org.jclouds.profitbricks.domain.specs;

import com.google.common.base.Strings;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.net.domain.IpProtocol;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a single Firewall Rule which allows traffic from
 * source to target dest.
 * 
 * @author Serj Sintsov
 */
public class FirewallRuleCreationSpec {

   public static Builder<?> builder() {
      return new FirewallRuleSpecBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected IpProtocol protocol;
      protected String sourceMac;
      protected String sourceIp;
      protected String targetIp;
      protected Integer fromPort;
      protected Integer toPort;
      protected Integer icmpType;
      protected Integer icmpCode;

      protected abstract T self();

      public FirewallRuleCreationSpec build() {
         checkFields();
         return buildInstance();
      }

      protected abstract FirewallRuleCreationSpec buildInstance();

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getProtocol()
       */
      public T protocol(IpProtocol protocol) {
         this.protocol = protocol;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getSourceMac()
       */
      public T sourceMac(String sourceMac) {
         this.sourceMac = sourceMac;
         return self();
      }


      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getSourceIp()
       */
      public T sourceIp(String sourceIp) {
         this.sourceIp = sourceIp;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getTargetIp()
       */
      public T targetIp(String targetIp) {
         this.targetIp = targetIp;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getFromPort()
       */
      public T fromPort(Integer fromPort) {
         this.fromPort = fromPort;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getToPort()
       */
      public T toPort(Integer toPort) {
         this.toPort = toPort;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getIcmpType()
       */
      public T icmpType(Integer icmpType) {
         this.icmpType = icmpType;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.FirewallRule#getIcmpCode()
       */
      public T icmpCode(Integer icmpCode) {
         this.icmpCode = icmpCode;
         return self();
      }

      protected void checkFields() {
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

   public static class FirewallRuleSpecBuilder extends Builder<FirewallRuleSpecBuilder> {
      @Override
      protected FirewallRuleSpecBuilder self() {
         return this;
      }

      @Override
      protected FirewallRuleCreationSpec buildInstance() {
         return new FirewallRuleCreationSpec( protocol, sourceMac, sourceIp, targetIp, fromPort, toPort, icmpType, icmpCode);
      }
   }

   private IpProtocol protocol;
   private String sourceMac;
   private String sourceIp;
   private String targetIp;
   private Integer fromPort;
   private Integer toPort;
   private Integer icmpType;
   private Integer icmpCode;

   protected FirewallRuleCreationSpec(IpProtocol protocol, String sourceMac, String sourceIp, String targetIp, Integer fromPort,
                                      Integer toPort, Integer icmpType, Integer icmpCode) {
      this.protocol = protocol;
      this.sourceMac = sourceMac;
      this.sourceIp = sourceIp;
      this.targetIp = targetIp;
      this.fromPort = fromPort;
      this.toPort = toPort;
      this.icmpType = icmpType;
      this.icmpCode = icmpCode;
   }

   public IpProtocol getProtocol() {
      return protocol;
   }

   public String getSourceMac() {
      return sourceMac;
   }

   public String getSourceIp() {
      return sourceIp;
   }

   public String getTargetIp() {
      return targetIp;
   }

   public Integer getFromPort() {
      return fromPort;
   }

   public Integer getToPort() {
      return toPort;
   }

   public Integer getIcmpType() {
      return icmpType;
   }

   @Nullable
   public Integer getIcmpCode() {
      return icmpCode;
   }

}
