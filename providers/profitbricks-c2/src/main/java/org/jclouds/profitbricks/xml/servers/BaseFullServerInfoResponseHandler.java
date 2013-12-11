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
package org.jclouds.profitbricks.xml.servers;

import com.google.common.collect.ImmutableMap;
import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.domain.NIC;
import org.jclouds.profitbricks.xml.BasePBResponseHandler;
import org.xml.sax.Attributes;

import javax.inject.Inject;

import java.util.Map;
import java.util.Stack;

import static org.jclouds.profitbricks.domain.Server.VirtualMachineState;

/**
 * Parses XML response which fully describes {@link Server} entity.
 *
 * @author Serj Sintsov
 */
public abstract class BaseFullServerInfoResponseHandler<T> extends BasePBResponseHandler<T> {

   protected enum ResponseGroup {
      SERVER,
      ROM_DRIVERS,
      NICS,
      STORAGES
   }

   private Map<String, ResponseGroup> tagsToGroups = ImmutableMap.of(
         "return", ResponseGroup.SERVER,
         "romDrives", ResponseGroup.ROM_DRIVERS,
         "nics", ResponseGroup.NICS,
         "connectedStorages", ResponseGroup.STORAGES
   );

   private Stack<ResponseGroup> processingGroups;

   protected Server.Builder<?> serverBuilder;
   protected NIC.Builder<?> nicBuilder;
   protected Server currentServer;

   @Inject
   public BaseFullServerInfoResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
      processingGroups = new Stack<ResponseGroup>();
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) {
      ResponseGroup gr = tagsToGroups.get(qName);

      if (gr == null) return;

      switch (gr) {
         case NICS:
            nicBuilder = NIC.builder();
            break;
         case SERVER:
            serverBuilder = Server.builder();
            break;
      }

      processingGroups.push(gr);
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      ResponseGroup gr = tagsToGroups.get(qName);

      if (gr == null)
         setElementaryField(qName);
      else
         endGroupOfElementaryFields(gr);

      clearTextBuffer();
   }

   protected void endGroupOfElementaryFields(ResponseGroup gr) {
      switch (gr) {
         case NICS:
            serverBuilder.addNIC(nicBuilder.build());
            processingGroups.pop();
            nicBuilder = null;
            break;
         case ROM_DRIVERS: case STORAGES:
            processingGroups.pop();
            break;
         case SERVER:
            currentServer = serverBuilder.build();
            processingGroups.pop();
            break;
      }
   }

   protected void setElementaryField(String qName) {
      if (processingGroups.empty())
         return;

      switch (processingGroups.peek()) {
         case NICS:
            setNICInfoOnEndElementEvent(qName);
            break;
         case SERVER:
            setServerInfoOnEndElementEvent(qName);
            break;
      }
   }

   protected void setNICInfoOnEndElementEvent(String qName) {
      if (qName.equals("nicId")) nicBuilder.id(trimAndGetTagStrValue());
      else if (qName.equals("ips")) nicBuilder.addIP(trimAndGetTagStrValue());
      else if (qName.equals("lanId")) nicBuilder.lanId(textBufferToIntValue());
      else if (qName.equals("nicName")) nicBuilder.nicName(trimAndGetTagStrValue());
      else if (qName.equals("serverId")) nicBuilder.serverId(trimAndGetTagStrValue());
      else if (qName.equals("gatewayIp")) nicBuilder.gatewayIp(trimAndGetTagStrValue());
      else if (qName.equals("macAddress")) nicBuilder.macAddress(trimAndGetTagStrValue());
      else if (qName.equals("dhcpActive")) nicBuilder.dhcpActive(textBufferToBoolValue());
      else if (qName.equals("internetAccess")) nicBuilder.internetAccess(textBufferToBoolValue());
      else if (qName.equals("provisioningState")) nicBuilder.provisioningState(ProvisioningState.fromValue(trimAndGetTagStrValue()));
   }

   protected void setServerInfoOnEndElementEvent(String qName) {
      if (qName.equals("ram")) serverBuilder.ram(textBufferToIntValue());
      else if (qName.equals("cores")) serverBuilder.cores(textBufferToIntValue());
      else if (qName.equals("osType")) serverBuilder.osType(OSType.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("serverId")) serverBuilder.id(trimAndGetTagStrValue());
      else if (qName.equals("serverName")) serverBuilder.serverName(trimAndGetTagStrValue());
      else if (qName.equals("dataCenterId")) serverBuilder.dataCenterId(trimAndGetTagStrValue());
      else if (qName.equals("creationTime")) serverBuilder.creationTime(textBufferToIso8601Date());
      else if (qName.equals("availabilityZone")) serverBuilder.availabilityZone(AvailabilityZone.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("provisioningState")) serverBuilder.provisioningState(ProvisioningState.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("virtualMachineState")) serverBuilder.virtualMachineState(VirtualMachineState.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("lastModificationTime")) serverBuilder.lastModificationTime(textBufferToIso8601Date());
   }

}
