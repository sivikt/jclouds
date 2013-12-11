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

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import static org.jclouds.profitbricks.domain.Server.VirtualMachineState.*;

/**
 * Test for {@link ServerStateToNodeMetadataStatus}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "ServerStateToNodeMetadataStatusTest")
public class ServerStateToNodeMetadataStatusTest {

   @Test
   public void testApply() {
      ServerStateToNodeMetadataStatus func = new ServerStateToNodeMetadataStatus();

      assertEquals(func.apply(server(RUNNING, ProvisioningState.DELETED)), NodeMetadata.Status.UNRECOGNIZED);
      assertEquals(func.apply(server(RUNNING, ProvisioningState.ERROR)), NodeMetadata.Status.ERROR);
      assertEquals(func.apply(server(RUNNING, ProvisioningState.INPROCESS)), NodeMetadata.Status.PENDING);

      assertEquals(func.apply(server(BLOCKED, ProvisioningState.INACTIVE)), NodeMetadata.Status.PENDING);
      assertEquals(func.apply(server(CRASHED, ProvisioningState.INACTIVE)), NodeMetadata.Status.ERROR);
      assertEquals(func.apply(server(NOSTATE, ProvisioningState.INACTIVE)), NodeMetadata.Status.UNRECOGNIZED);
      assertEquals(func.apply(server(PAUSED, ProvisioningState.INACTIVE)), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.apply(server(RUNNING, ProvisioningState.INACTIVE)), NodeMetadata.Status.RUNNING);
      assertEquals(func.apply(server(SHUTDOWN, ProvisioningState.INACTIVE)), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.apply(server(SHUTOFF, ProvisioningState.INACTIVE)), NodeMetadata.Status.SUSPENDED);

      assertEquals(func.apply(server(BLOCKED, ProvisioningState.AVAILABLE)), NodeMetadata.Status.PENDING);
      assertEquals(func.apply(server(CRASHED, ProvisioningState.AVAILABLE)), NodeMetadata.Status.ERROR);
      assertEquals(func.apply(server(NOSTATE, ProvisioningState.AVAILABLE)), NodeMetadata.Status.UNRECOGNIZED);
      assertEquals(func.apply(server(PAUSED, ProvisioningState.AVAILABLE)), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.apply(server(RUNNING, ProvisioningState.AVAILABLE)), NodeMetadata.Status.RUNNING);
      assertEquals(func.apply(server(SHUTDOWN, ProvisioningState.AVAILABLE)), NodeMetadata.Status.SUSPENDED);
      assertEquals(func.apply(server(SHUTOFF, ProvisioningState.AVAILABLE)), NodeMetadata.Status.SUSPENDED);
   }

   public Server server(Server.VirtualMachineState vmState, ProvisioningState provState) {
      return Server.builder()
            .virtualMachineState(vmState)
            .provisioningState(provState)
            .id("1")
            .dataCenterId("1")
            .cores(1)
            .ram(256)
            .build();
   }

}
