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
package org.jclouds.profitbricks.predicates;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import org.jclouds.logging.Logger;
import org.jclouds.profitbricks.PBApi;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.rest.ResourceNotFoundException;

import javax.annotation.Resource;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * Checks that the server is in {@link org.jclouds.profitbricks.domain.ProvisioningState#AVAILABLE} state.
 * 
 * @author Serj Sintsov
 */
@Singleton
public class ServerProvisionStateAvailable implements Predicate<Server> {

   @Resource
   protected Logger logger = Logger.NULL;

   private final PBApi client;

   @Inject
   public ServerProvisionStateAvailable(PBApi client) {
      this.client = checkNotNull(client, "client");
   }

   public boolean apply(Server server) {
      logger.trace("checking server state [id=%s]", server.getId());
      try {
         server = refresh(server);
         logger.trace("checking server [id=%s] state %s: currently: %s", server.getId(), ProvisioningState.AVAILABLE,
                      server.getProvisioningState());
         return server.getProvisioningState() == ProvisioningState.AVAILABLE;
      } catch (ResourceNotFoundException e) {
         return false;
      }
   }

   private Server refresh(Server server) {
      return client.serversApi().getServer(server.getId());
   }

}
