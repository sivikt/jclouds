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

import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;

/**
 * Use that to correctly update an existing {@link org.jclouds.profitbricks.domain.Server}'s properties.
 *
 * @author Serj Sintsov
 */
public class ServerUpdatingSpec extends ServerCreationSpec {

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder extends ServerCreationSpec.Builder {

      public ServerUpdatingSpec build() {
         checkFields();
         return buildInstance();
      }

      @Override
      protected ServerUpdatingSpec buildInstance() {
         return new ServerUpdatingSpec(serverName, cores, ram, osType, availabilityZone, bootFromImageId, bootFromStorageId);
      }

      /**
       * {@inheritDoc}
       */
      public Builder dataCenterId(String dataCenterId) {
         throw new UnsupportedOperationException("Impossible to change server's data center");
      }

      /**
       * {@inheritDoc}
       */
      public Builder serverName(String serverName) {
         this.serverName = serverName;
         return this;
      }

      /**
       * {@inheritDoc}
       */
      public Builder bootFromImageId(String bootFromImageId) {
         this.bootFromImageId = bootFromImageId;
         return this;
      }

      /**
       * {@inheritDoc}
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
       * {@inheritDoc}
       */
      public Builder ram(int ram) {
         this.ram = ram;
         return this;
      }

      /**
       * {@inheritDoc}
       */
      public Builder internetAccess(boolean internetAccess) {
         throw new UnsupportedOperationException("Impossible to change server's internet connection");
      }

      /**
       * {@inheritDoc}
       */
      public Builder availabilityZone(AvailabilityZone zone) {
         this.availabilityZone = zone;
         return this;
      }

      /**
       * {@inheritDoc}
       */
      public Builder osType(OSType osType) {
         this.osType = osType;
         return this;
      }
   }

   protected ServerUpdatingSpec(String serverName, int cores, int ram, OSType osType, AvailabilityZone availabilityZone,
                                String bootFromImageId, String bootFromStorageId) {
      super(null, serverName, cores, ram, null, osType, availabilityZone, bootFromImageId, bootFromStorageId);
   }

}
