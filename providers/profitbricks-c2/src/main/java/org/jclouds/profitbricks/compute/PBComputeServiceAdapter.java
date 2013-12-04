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
package org.jclouds.profitbricks.compute;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.*;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.logging.Logger;
import org.jclouds.profitbricks.PBApi;
import org.jclouds.profitbricks.domain.Server;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Designates connection between {@link org.jclouds.compute.ComputeService} API and
 * {@link org.jclouds.profitbricks.PBApi} API.
 *
 * TODO unit and live test
 *
 * @author Serj Sintsov
 */
@Singleton
public class PBComputeServiceAdapter implements ComputeServiceAdapter<Server, Hardware, Image, Location> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   protected PBApi pbApi;
   protected Function<Template, Server> templateToServer;

   @Inject
   public PBComputeServiceAdapter(PBApi pbApi, Function<Template, Server> templateToServer) {
      this.pbApi = checkNotNull(pbApi, "pbApi");
      this.templateToServer = checkNotNull(templateToServer, "templateToServer");
   }

   @Override
   public NodeAndInitialCredentials<Server> createNodeWithGroupEncodedIntoName(String group, String name, Template template) {
      Server serverToCreate = templateToServer.apply(template);

      logger.trace(">> creating new server from template [%s]", serverToCreate);
      String createdServerId = pbApi.serversApi().createServer(serverToCreate);
      if (createdServerId == null) {
         logger.trace("<< server creation failed. template [%s]", serverToCreate);
         return null; // TODO return exception when ComputeServiceAdapter will allow
      }
      logger.trace("<< server created with id=%s", createdServerId);

      logger.trace(">> getting server with id=%s", createdServerId);
      Server createdServer = pbApi.serversApi().getServer(createdServerId);
      logger.trace("<< got server [%s]", createdServer);

      return new NodeAndInitialCredentials<Server>(
         createdServer,
         createdServerId,
         LoginCredentials.builder().build()
      );
   }

   @Override
   public Iterable<Image> listImages() {
      logger.trace("listing of Images doesn't implemented yet. Return empty iterable collection");
      return ImmutableSet.of(new ImageBuilder()
            .id("fake")
            .status(Image.Status.AVAILABLE)
            .operatingSystem(OperatingSystem.builder()
                              .family(OsFamily.LINUX)
                              .description(OsFamily.LINUX.value())
                              .build())
            .build());  // todo tmp workaround. provide all possible images
   }

   @Override
   public Image getImage(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Iterable<Hardware> listHardwareProfiles() {
      logger.trace("listing of Hardware Profiles doesn't implemented yet. Return empty iterable collection");
      return ImmutableSet.of(); // TODO clarify does it really need; how to provide hardware profiles
   }

   @Override
   public Iterable<Location> listLocations() {
      logger.trace("listing of Locations doesn't implemented yet. Return empty iterable collection");
      return ImmutableSet.of(); // TODO clarify does it really need; how to provide locations
   }

   @Override
   public Iterable<Server> listNodes() {
      return pbApi.serversApi().getAllServers();
   }

   @Override
   public Server getNode(String id) {
      checkNotNull(id, "id");
      return pbApi.serversApi().getServer(id);
   }

   @Override
   public Iterable<Server> listNodesByIds(Iterable<String> ids) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public void destroyNode(String id) {
      checkNotNull(id, "id");
      pbApi.serversApi().deleteServer(id);
   }

   @Override
   public void rebootNode(String id) {
      checkNotNull(id, "id");
      pbApi.serversApi().resetServer(id);
   }

   @Override
   public void resumeNode(String id) {
      checkNotNull(id, "id");
      pbApi.serversApi().startServer(id);
   }

   @Override
   public void suspendNode(String id) {
      checkNotNull(id, "id");
      pbApi.serversApi().stopServer(id);
   }

}
