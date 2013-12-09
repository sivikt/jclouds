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
package org.jclouds.profitbricks.domain.options;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;

import static com.google.common.base.Preconditions.checkState;

/**
 * New {@link org.jclouds.profitbricks.domain.Server} entity specification.
 * Use this to correctly specify new {@link org.jclouds.profitbricks.domain.Server}
 * entity characteristics which you want to add in your cloud.
 *
 * @author Serj Sintsov
 */
public class ServerCreationSpec {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public abstract static class Builder<T extends Builder<T>> {

      protected String dataCenterId;
      protected String serverName;
      protected String bootFromImageId;
      protected int cores;
      protected int ram;
      protected boolean internetAccess;
      protected OSType osType;
      protected AvailabilityZone availabilityZone;

      protected abstract T self();

      public ServerCreationSpec build() {
         checkFields();
         return buildInstance();
      }

      protected abstract ServerCreationSpec buildInstance();

      /**
       * @see org.jclouds.profitbricks.domain.Server#getDataCenterId()
       */
      public T dataCenterId(String dataCenterId) {
         this.dataCenterId = dataCenterId;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getServerName()
       */
      public T serverName(String serverName) {
         this.serverName = serverName;
         return self();
      }

      /**
       * Id of the image which you want to use for this server
       */
      public T bootFromImageId(String bootFromImageId) {
         this.bootFromImageId = bootFromImageId;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getCores()
       */
      public T cores(int cores) {
         this.cores = cores;
         checkCores();
         return self();
      }

      private void checkCores() {
         checkState(cores > 0, "Number of core must be >=1");
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getRam()
       */
      public T ram(int ram) {
         this.ram = ram;
         checkRam();
         return self();
      }

      private void checkRam() {
         checkState(ram >= 256, "Minimal RAM size is 256 MiB");
      }

      /**
       * Connect this server to the Internet?
       */
      public T internetAccess(boolean internetAccess) {
         this.internetAccess = internetAccess;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getAvailabilityZone()
       */
      public T availabilityZone(AvailabilityZone zone) {
         this.availabilityZone = zone;
         return self();
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getOsType()
       */
      public T osType(OSType osType) {
         this.osType = osType;
         return self();
      }

      protected void checkFields() {
         checkCores();
         checkRam();
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }

      @Override
      protected ServerCreationSpec buildInstance() {
         return new ServerCreationSpec(dataCenterId, serverName, cores, ram, internetAccess, osType, availabilityZone,
                                        bootFromImageId);
      }
   }

   protected ServerCreationSpec(String dataCenterId, String serverName, int cores, int ram, boolean internetAccess,
                                OSType osType, AvailabilityZone availabilityZone, String bootFromImageId) {
      this.dataCenterId = Strings.emptyToNull(dataCenterId);
      this.serverName = Strings.emptyToNull(serverName);
      this.cores = cores;
      this.ram = ram;
      this.internetAccess = internetAccess;
      this.osType = osType;
      this.availabilityZone = availabilityZone;
      this.bootFromImageId = Strings.emptyToNull(bootFromImageId);
   }

   private String dataCenterId;
   private String serverName;
   private String bootFromImageId;

   private int cores;
   private int ram;

   private boolean internetAccess;

   private OSType osType;
   private AvailabilityZone availabilityZone;

   public String getDataCenterId() {
      return dataCenterId;
   }

   public String getServerName() {
      return serverName;
   }

   public String getBootFromImageId() {
      return bootFromImageId;
   }

   public int getCores() {
      return cores;
   }

   public int getRam() {
      return ram;
   }

   public boolean isInternetAccess() {
      return internetAccess;
   }

   public OSType getOsType() {
      return osType;
   }

   public AvailabilityZone getAvailabilityZone() {
      return availabilityZone;
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("dataCenterId", dataCenterId)
            .add("serverName", serverName)
            .add("internetAccess", internetAccess)
            .add("cores", cores)
            .add("ram", ram)
            .add("osType", osType)
            .add("availabilityZone", availabilityZone);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
