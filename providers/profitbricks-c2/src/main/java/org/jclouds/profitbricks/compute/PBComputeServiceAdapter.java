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

import com.google.common.collect.ImmutableSet;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
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
 * @author Serj Sintsov
 */
@Singleton
public class PBComputeServiceAdapter implements ComputeServiceAdapter<Server, Hardware, Image, Location> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   protected PBApi pbApi;

   @Inject
   public PBComputeServiceAdapter(PBApi pbApi) {
      this.pbApi = checkNotNull(pbApi, "pbApi");
   }

   @Override
   public NodeAndInitialCredentials<Server> createNodeWithGroupEncodedIntoName(String group, String name,
                                                                               Template template) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Iterable<Image> listImages() {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Image getImage(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Iterable<Hardware> listHardwareProfiles() {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Iterable<Location> listLocations() {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public Iterable<Server> listNodes() {
      if (!pbApi.serversApi().isPresent()) return ImmutableSet.of();
      return pbApi.serversApi().get().getAllServers();
   }

   @Override
   public Server getNode(String id) {
      checkNotNull(id, "id");
      if (!pbApi.serversApi().isPresent()) return null;
      return pbApi.serversApi().get().getServer(id);
   }

   @Override
   public Iterable<Server> listNodesByIds(Iterable<String> ids) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public void destroyNode(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public void rebootNode(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public void resumeNode(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

   @Override
   public void suspendNode(String id) {
      throw new UnsupportedOperationException("Isn't implemented yet");
   }

}
