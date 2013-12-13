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

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {

      protected String dataCenterId;
      protected String serverName;
      protected String bootFromImageId;
      protected String bootFromStorageId;
      protected int cores;
      protected int ram;
      protected Boolean internetAccess;
      protected OSType osType;
      protected AvailabilityZone availabilityZone;

      public ServerCreationSpec build() {
         checkFields();
         return buildInstance();
      }

      protected ServerCreationSpec buildInstance() {
         return new ServerCreationSpec(dataCenterId, serverName, cores, ram, internetAccess, osType, availabilityZone,
                                       bootFromImageId, bootFromStorageId);
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getDataCenterId()
       */
      public Builder dataCenterId(String dataCenterId) {
         this.dataCenterId = dataCenterId;
         return this;
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getServerName()
       */
      public Builder serverName(String serverName) {
         this.serverName = serverName;
         return this;
      }

      /**
       * Defines an existing CD-ROM/DVD image ID to be set as boot device of the server.
       * Note that only one boot device can be used at the same time.
       */
      public Builder bootFromImageId(String bootFromImageId) {
         this.bootFromImageId = bootFromImageId;
         return this;
      }

      /**
       * Defines an existing storage device ID to be set as boot device of the server.
       * Note that only one boot device can be used at the same time.
       */
      public Builder bootFromStorageId(String bootFromStorageId) {
         this.bootFromStorageId = bootFromStorageId;
         return this;
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getCores()
       */
      public Builder cores(int cores) {
         this.cores = cores;
         return this;
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getRam()
       */
      public Builder ram(int ram) {
         this.ram = ram;
         return this;
      }
      /**
       * Connect this server to the Internet?
       */
      public Builder internetAccess(boolean internetAccess) {
         this.internetAccess = internetAccess;
         return this;
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getAvailabilityZone()
       */
      public Builder availabilityZone(AvailabilityZone zone) {
         this.availabilityZone = zone;
         return this;
      }

      /**
       * @see org.jclouds.profitbricks.domain.Server#getOsType()
       */
      public Builder osType(OSType osType) {
         this.osType = osType;
         return this;
      }

      protected void checkFields() {
         checkState(cores > 0, "Number of core must be >=1");
         checkState(ram >= 256, "Minimal RAM size is 256 MiB");

         dataCenterId = Strings.emptyToNull(dataCenterId);
         serverName = Strings.emptyToNull(serverName);
         bootFromImageId = Strings.emptyToNull(bootFromImageId);
         bootFromStorageId = Strings.emptyToNull(bootFromStorageId);

         checkState(!(bootFromImageId != null && bootFromStorageId != null), "Only one boot device can be used at the same time");
      }
   }

   protected ServerCreationSpec(String dataCenterId, String serverName, int cores, int ram, Boolean internetAccess,
                                OSType osType, AvailabilityZone availabilityZone, String bootFromImageId,
                                String bootFromStorageId) {
      this.dataCenterId = dataCenterId;
      this.serverName = serverName;
      this.cores = cores;
      this.ram = ram;
      this.internetAccess = internetAccess;
      this.osType = osType;
      this.availabilityZone = availabilityZone;
      this.bootFromImageId = bootFromImageId;
      this.bootFromStorageId = bootFromStorageId;
   }

   protected String dataCenterId;
   protected String serverName;
   protected String bootFromImageId;
   protected String bootFromStorageId;

   protected int cores;
   protected int ram;

   protected Boolean internetAccess;

   protected OSType osType;
   protected AvailabilityZone availabilityZone;

   public String getDataCenterId() {
      return dataCenterId;
   }

   public String getServerName() {
      return serverName;
   }

   public String getBootFromImageId() {
      return bootFromImageId;
   }

   public String getBootFromStorageId() {
      return bootFromStorageId;
   }

   public int getCores() {
      return cores;
   }

   public int getRam() {
      return ram;
   }

   public Boolean isInternetAccess() {
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
            .add("bootFromStorageId", bootFromStorageId)
            .add("bootFromImageId", bootFromImageId)
            .add("availabilityZone", availabilityZone);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
