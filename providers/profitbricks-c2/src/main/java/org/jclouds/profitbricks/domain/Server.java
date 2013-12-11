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
import org.jclouds.javax.annotation.Nullable;

import java.util.Date;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents information about Virtual Server, such as configuration,
 * provisioning status, power status, etc.
 *
 * @author Serj Sintsov
 */
public class Server {

   public static ServerDescribingBuilder builder() {
      return new ServerDescribingBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected String id;
      protected String dataCenterId;
      protected String serverName;
      protected int cores;
      protected int ram;
      protected OSType osType;
      protected AvailabilityZone availabilityZone;
      protected Date creationTime;
      protected Date lastModificationTime;
      protected ProvisioningState provisioningState;
      protected VirtualMachineState virtualMachineState;
      protected Set<NIC> nics = Sets.newLinkedHashSet(); // TODO may be its better to keep here only nics ids

      protected abstract T self();

      public Server build() {
         checkFields();
         return buildInstance();
      }

      protected abstract Server buildInstance();

      /**
       * @see Server#getDataCenterId()
       */
      public T dataCenterId(String dataCenterId) {
         this.dataCenterId = dataCenterId;
         return self();
      }

      /**
       * @see Server#getServerName()
       */
      public T serverName(String serverName) {
         this.serverName = serverName;
         return self();
      }

      /**
       * @see Server#getCores()
       */
      public T cores(int cores) {
         this.cores = cores;
         return self();
      }

      /**
       * @see Server#getRam()
       */
      public T ram(int ram) {
         this.ram = ram;
         return self();
      }

      /**
       * @see Server#getAvailabilityZone()
       */
      public T availabilityZone(AvailabilityZone zone) {
         this.availabilityZone = zone;
         return self();
      }

      /**
       * @see Server#getOsType()
       */
      public T osType(OSType osType) {
         this.osType = osType;
         return self();
      }

      /**
       * @see Server#getId()
       */
      public T id(String id) {
         this.id = id;
         return self();
      }

      /**
       * @see Server#getCreationTime()
       */
      public T creationTime(Date creationTime) {
         this.creationTime = creationTime;
         return self();
      }

      /**
       * @see Server#getLastModificationTime()
       */
      public T lastModificationTime(Date lastModificationTime) {
         this.lastModificationTime = lastModificationTime;
         return self();
      }

      /**
       * @see Server#getProvisioningState()
       */
      public T provisioningState(ProvisioningState provisioningState) {
         this.provisioningState = provisioningState;
         return self();
      }

      /**
       * @see Server#getVirtualMachineState()
       */
      public T virtualMachineState(VirtualMachineState virtualMachineState) {
         this.virtualMachineState = virtualMachineState;
         return self();
      }

      /**
       * @see Server#getNics()
       */
      public T addNIC(NIC nic) {
         checkNotNull(nic, "nic");
         nics.add(nic);
         return self();
      }

      protected void checkFields() {
         checkState(cores > 0, "Number of core must be >=1");
         checkState(ram >= 256, "Minimal RAM size is 256 MiB");
         checkNotNull(id, "id");
         checkNotNull(dataCenterId, "id");
         checkNotNull(provisioningState, "provisioningState");
         checkNotNull(virtualMachineState, "virtualMachineState");

         for (NIC nic : nics)
            checkState(id.equals(nic.getServerId()), "nic '%s' doesn't match server's id '%s'", nic.getId(), id);
         nics = ImmutableSet.copyOf(nics);

         availabilityZone = availabilityZone == null ? AvailabilityZone.AUTO : availabilityZone;  // TODO find checkReturnDefault..or something
         osType = osType == null ? OSType.UNKNOWN : osType;
      }
   }

   public static class ServerDescribingBuilder extends Builder<ServerDescribingBuilder> {
      @Override
      protected ServerDescribingBuilder self() {
         return this;
      }

      @Override
      protected Server buildInstance() {
         return new Server(dataCenterId, id, serverName, cores, ram, osType, availabilityZone, creationTime,
                           lastModificationTime, provisioningState, virtualMachineState, nics);
      }
   }

   protected Server(String dataCenterId, String id, String serverName, int cores, int ram, OSType osType,
                    AvailabilityZone availabilityZone, Date creationTime, Date lastModificationTime,
                    ProvisioningState provisioningState, VirtualMachineState virtualMachineState, Set<NIC> nics) {
      this.id = id;
      this.dataCenterId = dataCenterId;
      this.serverName = serverName;
      this.cores = cores;
      this.ram = ram;
      this.osType = osType;
      this.availabilityZone = availabilityZone;
      this.creationTime = creationTime;
      this.lastModificationTime = lastModificationTime;
      this.provisioningState = provisioningState;
      this.virtualMachineState = virtualMachineState;
      this.nics = nics;
   }

   public enum VirtualMachineState {
      NOSTATE, RUNNING, BLOCKED, PAUSED, SHUTDOWN, SHUTOFF, CRASHED;

      public String value() {
         return name();
      }

      public static VirtualMachineState fromValue(String value) {
         return valueOf(value);
      }
   }

   private String dataCenterId;
   private String id;
   private String serverName;
   private int cores;
   private int ram;
   private Date creationTime;
   private Date lastModificationTime;
   private ProvisioningState provisioningState;
   private VirtualMachineState virtualMachineState;
   private OSType osType;
   private AvailabilityZone availabilityZone;
   private Set<NIC> nics;

   /**
    * Defines the data center wherein the server is to be created.
    */
   public String getDataCenterId() {
      return dataCenterId;
   }

   /**
    * Identifier of the virtual server
    */
   public String getId() {
      return id;
   }

   /**
    * Outputs the name of the specified virtual server
    */
   @Nullable
   public String getServerName() {
      return serverName;
   }


   /**
    * Number of cores to be assigned to the specified server
    */
   public int getCores() {
      return cores;
   }

   /**
    * Number of RAM memory (in MiB) to be assigned to the server.
    * The minimum RAM size is 256 MiB
    */
   public int getRam() {
      return ram;
   }

   /**
    * Time when the specified virtual server has been created
    */
   @Nullable
   public Date getCreationTime() {
      return creationTime;
   }

   /**
    * Time when the specified virtual server has last been modified
    */
   @Nullable
   public Date getLastModificationTime() {
      return lastModificationTime;
   }

   /**
    * Describes the current provisioning state of the specified virtual server
    */
   public ProvisioningState getProvisioningState() {
      return provisioningState;
   }

   /**
    * Describes the current server state of the specified virtual server
    */
   public VirtualMachineState getVirtualMachineState() {
      return virtualMachineState;
   }

   /**
    * OS type of the server
    */
   public OSType getOsType() {
      return osType;
   }

   /**
    * Zone in which the server is located
    */
   public AvailabilityZone getAvailabilityZone() {
      return availabilityZone;
   }

   /**
    * Lists all NICs assigned to the server.
    */
   public Set<NIC> getNics() {
      return nics;
   }

   /**
    * Returns {@code true} if server is connected to a public LAN
    */
   public boolean isInternetAccess() {
      for (NIC nic : nics)
         if (nic.isInternetAccess())
            return true;
      return false;
   }

   /**
    * Lists all IP addresses assigned to the server
    */
   public Set<String> getIPs() {
      Set<String> ips = Sets.newHashSet();
      for (NIC nic : nics)
         ips.addAll(nic.getIps());

      return ips;
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
      Server that = Server.class.cast(obj);

      return Objects.equal(this.id, that.id);
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("id", id)
            .add("id", dataCenterId)
            .add("serverName", serverName)
            .add("creationTime", creationTime)
            .add("lastModificationTime", lastModificationTime)
            .add("cores", cores)
            .add("ram", ram)
            .add("osType", osType)
            .add("provisioningState", provisioningState)
            .add("virtualMachineState", virtualMachineState)
            .add("availabilityZone", availabilityZone)
            .add("nics", nics);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}
