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
package org.jclouds.profitbricks.compute.functions;

import com.google.common.base.Function;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A function for transforming a ProfitBricks {@link org.jclouds.profitbricks.domain.Server} into a generic
 * {@link org.jclouds.compute.domain.NodeMetadata} object.
 *
 * @author Serj Sintsov
 */
public class ServerStateToNodeMetadataStatus implements Function<Server, NodeMetadata.Status> {

   @Override
   public NodeMetadata.Status apply(Server server) {
      checkNotNull(server, "server");

      ProvisioningState provisioningState = server.getProvisioningState();
      Server.VirtualMachineState vmState  = server.getVirtualMachineState();

      if (provisioningState == null) {
         return NodeMetadata.Status.UNRECOGNIZED;
      }

      switch (provisioningState) {
         case INPROCESS:  return NodeMetadata.Status.PENDING;
         case ERROR: return NodeMetadata.Status.ERROR;
         case DELETED: return NodeMetadata.Status.UNRECOGNIZED;
      }

      if (provisioningState == ProvisioningState.AVAILABLE || provisioningState == ProvisioningState.INACTIVE) {
         if (vmState == null) {
            return NodeMetadata.Status.UNRECOGNIZED;
         }

         switch (vmState) {
            case SHUTDOWN:
            case SHUTOFF:
            case PAUSED:  return NodeMetadata.Status.SUSPENDED;
            case RUNNING: return NodeMetadata.Status.RUNNING;
            case BLOCKED: return NodeMetadata.Status.PENDING;
            case CRASHED: return NodeMetadata.Status.ERROR;
            default:      return NodeMetadata.Status.UNRECOGNIZED;
         }
      }

      return NodeMetadata.Status.UNRECOGNIZED;
   }

}
