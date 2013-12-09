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

import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.xml.BasePBResponseHandler;

import javax.inject.Inject;

import static org.jclouds.profitbricks.domain.Server.*;

/**
 * Parses XML response which fully describes {@link Server} entity.
 *
 * @author Serj Sintsov
 */
public abstract class BaseFullServerInfoResponseHandler<T> extends BasePBResponseHandler<T> {

   protected DescribingBuilder describingBuilder;

   @Inject
   public BaseFullServerInfoResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
      describingBuilder = Server.builder();
   }

   protected void setServerInfoOnEndElementEvent(String qName) {
      if (qName.equals("ram")) describingBuilder.ram(textBufferToIntValue());
      else if (qName.equals("cores")) describingBuilder.cores(textBufferToIntValue());
      else if (qName.equals("osType")) describingBuilder.osType(OSType.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("serverId")) describingBuilder.serverId(trimAndGetTagStrValue());
      else if (qName.equals("serverName")) describingBuilder.serverName(trimAndGetTagStrValue());
      else if (qName.equals("dataCenterId")) describingBuilder.dataCenterId(trimAndGetTagStrValue());
      else if (qName.equals("creationTime")) describingBuilder.creationTime(textBufferToIso8601Date());
      else if (qName.equals("availabilityZone")) describingBuilder.availabilityZone(AvailabilityZone.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("provisioningState")) describingBuilder.provisioningState(ProvisioningState.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("virtualMachineState")) describingBuilder.virtualMachineState(VirtualMachineState.fromValue(trimAndGetTagStrValue()));
      else if (qName.equals("lastModificationTime")) describingBuilder.lastModificationTime(textBufferToIso8601Date());
   }

}
