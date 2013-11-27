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
package org.jclouds.profitbricks.config;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.profitbricks.compute.PBComputeServiceAdapter;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.functions.ServerToNodeMetadata;

/**
 * Configuration module with bindings to setup ProfitBricks {@link ComputeServiceAdapter}.
 *
 * @author Serj Sintsov
 */
public class PBComputeServiceAdapterContextModule
      extends ComputeServiceAdapterContextModule<Server, Hardware, Image, Location> {

   @SuppressWarnings("unchecked")
   @Override
   protected void configure() {
      bind(new TypeLiteral<ComputeServiceAdapter<Server, Hardware, Image, Location>>() {
      }).to(PBComputeServiceAdapter.class);

      bind(new TypeLiteral<Function<Server, NodeMetadata>>() {
      }).to(Class.class.cast(ServerToNodeMetadata.class));

      super.configure();
   }

}
