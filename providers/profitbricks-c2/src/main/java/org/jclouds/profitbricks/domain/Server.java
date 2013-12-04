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
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents information about Virtual Server, such as configuration,
 * provisioning status, power status, etc.
 *
 * TODO move builders to separate files. Revise fields rules
 *
 * @author Serj Sintsov
 */
public class Server {

   public static ServerCreationBuilder creationBuilder() {
      return new ServerCreationBuilder();
   }

   public static ServerDescribingBuilder describingBuilder() {
      return new ServerDescribingBuilder();
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected String dataCenterId;
      protected String serverName;
      protected String bootFromImageId;
      protected int cores;
      protected int ram;
      protected boolean internetAccess;
      protected OSType osType;
      protected AvailabilityZone availabilityZone;

      protected abstract T self();

      public Server build() {
         checkFields();
         return buildInstance();
      }

      protected abstract Server buildInstance();

      /**
       * If left empty, the server will be created in a new data center.
       *
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
       * @see Server#getBootFromImageId()
       */
      public T bootFromImageId(String bootFromImageId) {
         this.bootFromImageId = bootFromImageId;
         return self();
      }

      /**
       * @see Server#getCores()
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
       * The minimum RAM size is 256 MiB.
       *
       * @see Server#getRam()
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
       * Set to TRUE to connect the server to the Internet via the specified
       * LAN ID. If the LAN is not specified, it is going to be created in
       * the next available LAN ID, starting with LAN ID 1.
       *
       * @see Server#isInternetAccess()
       */
      public T internetAccess(boolean internetAccess) {
         this.internetAccess = internetAccess;
         return self();
      }

      /**
       * Sets the zone in which the server is going to be created.
       * Servers from different zones are located in different physical locations.
       * If set to {@link AvailabilityZone#AUTO} or left empty, servers will be
       * created in a random zone.
       *
       * @see Server#getAvailabilityZone()
       */
      public T availabilityZone(AvailabilityZone zone) {
         this.availabilityZone = zone;
         return self();
      }

      /**
       * If left empty, the server will inherit the OS Type of its selected
       * boot image/storage.
       *
       * @see Server#getOsType()
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

   /**
    * Use this builder to correctly create an new {@link Server} entity which you want to
    * add in your cloud.
    */
   public static class ServerCreationBuilder extends Builder<ServerCreationBuilder> {
      @Override
      protected ServerCreationBuilder self() {
         return this;
      }

      @Override
      protected Server buildInstance() {
         return new Server(dataCenterId, serverName, cores, ram, internetAccess, osType, availabilityZone, bootFromImageId);
      }
   }

   /**
    * Use this builder to create an existing server instance from any source.
    */
   public static class ServerDescribingBuilder extends Builder<ServerDescribingBuilder> {

      protected String serverId;
      protected Date creationTime;
      protected Date lastModificationTime;
      protected ProvisioningState provisioningState;
      protected VirtualMachineState virtualMachineState;

      /**
       * @see Server#getServerId()
       */
      public ServerDescribingBuilder serverId(String serverId) {
         this.serverId = checkNotNull(serverId, "serverId");
         return self();
      }

      /**
       * @see Server#getCreationTime()
       */
      public ServerDescribingBuilder creationTime(Date creationTime) {
         this.creationTime = creationTime;
         return self();
      }

      /**
       * @see Server#getLastModificationTime()
       */
      public ServerDescribingBuilder lastModificationTime(Date lastModificationTime) {
         this.lastModificationTime = lastModificationTime;
         return self();
      }

      /**
       * @see Server#getProvisioningState()
       */
      public ServerDescribingBuilder provisioningState(ProvisioningState provisioningState) {
         this.provisioningState = checkNotNull(provisioningState);
         return self();
      }

      /**
       * @see Server#getVirtualMachineState()
       */
      public ServerDescribingBuilder virtualMachineState(VirtualMachineState virtualMachineState) {
         this.virtualMachineState = checkNotNull(virtualMachineState);
         return self();
      }

      @Override
      protected ServerDescribingBuilder self() {
         return this;
      }

      @Override
      protected void checkFields() {
         super.checkFields();
         checkNotNull(serverId, "serverId");
         checkNotNull(dataCenterId, "dataCenterId");
         availabilityZone = availabilityZone == null ? AvailabilityZone.AUTO : availabilityZone;  // TODO find checkReturnDefault..or something
         osType = osType == null ? OSType.UNKNOWN : osType;
         provisioningState = provisioningState == null ? ProvisioningState.UNRECOGNIZED : provisioningState;
         virtualMachineState = virtualMachineState == null ? VirtualMachineState.UNRECOGNIZED : virtualMachineState;
      }

      @Override
      protected Server buildInstance() {
         return new Server(dataCenterId, serverId, serverName, cores, ram, internetAccess, osType, availabilityZone,
                           creationTime, lastModificationTime, provisioningState, virtualMachineState, bootFromImageId);
      }
   }

   protected Server(String dataCenterId, String serverName, int cores, int ram, boolean internetAccess, OSType osType,
                    AvailabilityZone availabilityZone, String bootFromImageId) {
      this(dataCenterId, null, serverName, cores, ram, internetAccess, osType, availabilityZone, null, null, null, null,
           bootFromImageId);
   }

   protected Server(String dataCenterId, String serverId, String serverName, int cores, int ram, boolean internetAccess,
                    OSType osType, AvailabilityZone availabilityZone, Date creationTime, Date lastModificationTime,
                    ProvisioningState provisioningState, VirtualMachineState virtualMachineState, String bootFromImageId) {
      this.dataCenterId = Strings.emptyToNull(dataCenterId);
      this.serverId = serverId;
      this.serverName = Strings.emptyToNull(serverName);
      this.cores = cores;
      this.ram = ram;
      this.internetAccess = internetAccess;
      this.osType = osType;
      this.availabilityZone = availabilityZone;
      this.creationTime = creationTime;
      this.lastModificationTime = lastModificationTime;
      this.provisioningState = provisioningState;
      this.virtualMachineState = virtualMachineState;
      this.bootFromImageId = Strings.emptyToNull(bootFromImageId);
   }

   public enum VirtualMachineState {
      NOSTATE, RUNNING, BLOCKED, PAUSED, SHUTDOWN, SHUTOFF, CRASHED, UNRECOGNIZED;

      public String value() {
         return name();
      }

      public static VirtualMachineState fromValue(String value) {
         try {
            return valueOf(value);
         } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
         }
      }
   }

   public enum OSType {
      WINDOWS, LINUX, OTHER, UNKNOWN;

      public String value() {
         return name();
      }

      public static OSType fromValue(String value) {
         try {
            return valueOf(value);
         } catch (IllegalArgumentException e) {
            return UNKNOWN;
         }
      }
   }

   public enum AvailabilityZone {
      AUTO, ZONE_1, ZONE_2;

      public String value() {
         return name();
      }

      public static AvailabilityZone fromValue(String value) {
         try {
            return valueOf(value);
         } catch (IllegalArgumentException e) {
            return AUTO;
         }
      }

      public Location toLocation() {
         return toLocation(null);
      }

      public Location toLocation(Location parent) {
         return new LocationBuilder()
               .id(value())
               .description(value())
               .scope(LocationScope.ZONE)
               .parent(parent)
               .build();
      }
   }

   private String dataCenterId;
   private String serverId;
   private String serverName;
   private String bootFromImageId;

   private int cores;
   private int ram;

   private boolean internetAccess;

   private Date creationTime;
   private Date lastModificationTime;

   private ProvisioningState provisioningState;
   private VirtualMachineState virtualMachineState;

   private OSType osType;
   private AvailabilityZone availabilityZone;

   /**
    * Defines the data center wherein the server is to be created.
    */
   public String getDataCenterId() {
      return dataCenterId;
   }

   /**
    * Identifier of the virtual server
    */
   public String getServerId() {
      return serverId;
   }

   /**
    * Emptiable. Outputs the name of the specified virtual server
    */
   public String getServerName() {
      return serverName;
   }

   /**
    * TODO investigate should we use it here or not. As for now this field is for only server creation
    * Defines an existing CD-ROM/DVD image ID to be set as boot device of the server.
    * A virtual CD-ROM/DVD drive with the mounted image will be connected to the server.
    */
   public String getBootFromImageId() {
      return bootFromImageId;
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
    * Returns {@code true} if server is connected to a public LAN
    */
   public boolean isInternetAccess() {
      return internetAccess;
   }

   /**
    * Nullable. Time when the specified virtual server has been created
    */
   public Date getCreationTime() {
      return creationTime;
   }

   /**
    * Nullable. Time when the specified virtual server has last been modified
    */
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

   @Override
   public int hashCode() {
      return Objects.hashCode(serverId);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      Server that = Server.class.cast(obj);

      return Objects.equal(this.serverId, that.serverId);
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "{" +
            "dataCenterId=" + dataCenterId + "," +
            "serverId=" + serverId + "," +
            "serverName=" + serverName + "," +
            "internetAccess=" + internetAccess + "," +
            "creationTime=" + creationTime + "," +
            "lastModificationTime=" + lastModificationTime + "," +
            "cores=" + cores + "," +
            "ram=" + ram + "," +
            "osType=" + osType + "," +
            "provisioningState=" + provisioningState + "," +
            "virtualMachineState=" + virtualMachineState + "," +
            "availabilityZone=" + availabilityZone +
      "}";
   }

}
