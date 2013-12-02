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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Describes Virtual Data Center which aggregates nodes.
 *
 * @author Serj Sintsov
 */
public class DataCenter {

   public static DataCenterCreationBuilder creationBuilder() {
      return new DataCenterCreationBuilder();
   }

   public static DataCenterDescribingBuilder describingBuilder() {
      return new DataCenterDescribingBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected String dataCenterName;
      protected DataCenterRegion region;

      protected abstract T self();

      public DataCenter build() {
         checkFields();
         return buildInstance();
      }

      protected abstract DataCenter buildInstance();

      /**
       * @see DataCenter#getDataCenterName()
       */
      public T dataCenterName(String dataCenterName) {
         this.dataCenterName = dataCenterName;
         return self();
      }

      /**
       * @see DataCenter#getRegion()
       */
      public T region(DataCenterRegion region) {
         this.region = region;
         return self();
      }

      protected void checkFields() { }
   }

   /**
    * Use this builder to correctly create an new {@link DataCenter} entity which you want to
    * make a root of your cloud.
    */
   public static class DataCenterCreationBuilder extends Builder<DataCenterCreationBuilder> {
      @Override
      protected DataCenterCreationBuilder self() {
         return this;
      }

      @Override
      protected DataCenter buildInstance() {
         return new DataCenter(null, dataCenterName, null, region);
      }
   }

   /**
    * Use this builder to describe an existing data center instance from any source.
    */
   public static class DataCenterDescribingBuilder extends Builder<DataCenterDescribingBuilder> {

      protected String dataCenterId;
      protected ProvisioningState provisioningState;

      /**
       * @see DataCenter#getDataCenterId()
       */
      public DataCenterDescribingBuilder dataCenterId(String dataCenterId) {
         this.dataCenterId = checkNotNull(dataCenterId, "dataCenterId");
         return self();
      }

      /**
       * @see DataCenter#getProvisioningState()
       */
      public DataCenterDescribingBuilder provisioningState(ProvisioningState provisioningState) {
         this.provisioningState = checkNotNull(provisioningState);
         return self();
      }

      @Override
      protected DataCenterDescribingBuilder self() {
         return this;
      }

      @Override
      protected void checkFields() {
         super.checkFields();
         checkNotNull(dataCenterId, "dataCenterId");
         provisioningState = provisioningState == null ? ProvisioningState.UNRECOGNIZED : provisioningState;
      }

      @Override
      protected DataCenter buildInstance() {
         return new DataCenter(dataCenterId, dataCenterName, provisioningState, region);
      }
   }

   protected DataCenter(String dataCenterName, DataCenterRegion region) {
      this(null, dataCenterName, null, region);
   }

   protected DataCenter(String dataCenterId, String dataCenterName, ProvisioningState provisioningState,
                        DataCenterRegion region) {
      this.dataCenterId = dataCenterId;
      this.dataCenterName = dataCenterName;
      this.provisioningState = provisioningState;
      this.region = region;
   }

   public enum DataCenterRegion {
      NORTH_AMERICA, EUROPE, DEFAULT;

      public String value() {
         return name();
      }

      public static DataCenterRegion fromValue(String value) {
         try {
            return valueOf(value);
         } catch (IllegalArgumentException e) {
            return DEFAULT;
         }
      }
   }

   private String dataCenterId;
   private String dataCenterName;
   private ProvisioningState provisioningState;
   private DataCenterRegion region;

   /**
    * Identifier of the virtual data center
    */
   public String getDataCenterId() {
      return dataCenterId;
   }

   /**
    * Name of the new virtual data center
    */
   public String getDataCenterName() {
      return dataCenterName;
   }

   /**
    * Region where the data center has been created
    */
   public DataCenterRegion getRegion() {
      return region;
   }

   /**
    * Describes the current state of the data center
    */
   public ProvisioningState getProvisioningState() {
      return provisioningState;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(dataCenterId);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      DataCenter that = DataCenter.class.cast(obj);

      return Objects.equal(this.dataCenterId, that.dataCenterId);
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "{" +
            "dataCenterId=" + dataCenterId + "," +
            "dataCenterName=" + dataCenterName + "," +
            "region=" + region +
      "}";
   }

}
