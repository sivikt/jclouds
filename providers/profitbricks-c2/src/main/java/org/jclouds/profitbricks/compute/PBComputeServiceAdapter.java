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
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.logging.Logger;
import org.jclouds.profitbricks.PBApi;
import org.jclouds.profitbricks.compute.options.PBTemplateOptions;
import org.jclouds.profitbricks.domain.NIC;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_NODE_RUNNING;

/**
 * Designates connection between {@link org.jclouds.compute.ComputeService} API and
 * {@link org.jclouds.profitbricks.PBApi} API.
 *
 * TODO Seems this is a temporary solution. As for now adapter provides basics to manipulate with nodes.
 * TODO After all provider specific API will be completed this class should be revised.
 *
 * @author Serj Sintsov
 */
@Singleton
public class PBComputeServiceAdapter implements ComputeServiceAdapter<Server, Hardware, Image, Location> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   protected final PBApi pbApi;
   protected final Function<Template, ServerCreationSpec> templateToServerSpec;
   protected final Predicate<Server> serverAvailable;
   protected final Function<Server, NodeMetadata> serverToNodeMetadata;

   @Inject
   public PBComputeServiceAdapter(PBApi pbApi, Function<Template, ServerCreationSpec> templateToServerSpec,
                                  @Named(TIMEOUT_NODE_RUNNING) Predicate<Server> serverAvailable,
                                  Function<Server, NodeMetadata> serverToNodeMetadata) {
      this.pbApi = checkNotNull(pbApi, "pbApi");
      this.templateToServerSpec = checkNotNull(templateToServerSpec, "templateToServerSpec");
      this.serverAvailable = checkNotNull(serverAvailable, "serverAvailable");
      this.serverToNodeMetadata = checkNotNull(serverToNodeMetadata, "serverToNodeMetadata");
   }

   @Override
   public NodeAndInitialCredentials<Server> createNodeWithGroupEncodedIntoName(String group, String name, Template template) {
      checkArgument(template.getOptions().getClass().isAssignableFrom(PBTemplateOptions.class),
                    "template option's class must be assignable from " + PBTemplateOptions.class.getCanonicalName());

      PBTemplateOptions pbTplOpts = template.getOptions().as(PBTemplateOptions.class);
      checkNotNull(pbTplOpts.getServerSpec(), "ServerSpec has to be specified");

      logger.trace(">> creating server from template [%s]", pbTplOpts.getServerSpec());
      String createdServerId = pbApi.serversApi().createServer(pbTplOpts.getServerSpec());
      if (createdServerId == null) {
         logger.trace("<< server creation failed. template [%s]", pbTplOpts.getServerSpec());
         return null;
      }
      logger.trace("<< server created with id=%s", createdServerId);

      Server createdServer = blockUntilServerProvisionStateAvailable(createdServerId);

      if (!pbTplOpts.getFirewallRuleSpecs().isEmpty())
         createdServer = setupFirewallRules(createdServer, pbTplOpts.getFirewallRuleSpecs());

      return new NodeAndInitialCredentials<Server>(
         createdServer,
         createdServerId,
         LoginCredentials.builder().build()
      );
   }

   private Server setupFirewallRules(Server server, Set<FirewallRuleCreationSpec> firewallRules) {
      logger.trace(">> adding firewall rules to server [id=%s]", server.getId());

      if (server.getNics().isEmpty()) {
         logger.trace("<< to add firewall rules server [id=%s] must have at least one NIC", server.getId());
         return server;
      }

      NIC nic = Iterables.getOnlyElement(server.getNics());
      for (FirewallRuleCreationSpec ruleSpec : firewallRules)
         pbApi.firewallApi().addFirewallRule(nic.getId(), ruleSpec);

      server = blockUntilServerProvisionStateAvailable(server.getId());

      logger.trace(">> adding firewall rules to server [id=%s]", server.getId());
      return server;
   }

   private Server blockUntilServerProvisionStateAvailable(String serverId) {
      logger.trace(">> awaiting status running for server [%s]", serverId);
      Server createdServer = pbApi.serversApi().getServer(serverId);
      serverAvailable.apply(createdServer);
      logger.trace("<< server is running [%s]", createdServer);

      return pbApi.serversApi().getServer(serverId);
   }

   @Override
   public Iterable<Image> listImages() {
      logger.trace("listing of Images doesn't implemented yet. Return empty iterable collection");
      return ImmutableSet.of();  // TODO clarify does it really need; how to provide hardware profiles
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
      return pbApi.serversApi().listServers();
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
