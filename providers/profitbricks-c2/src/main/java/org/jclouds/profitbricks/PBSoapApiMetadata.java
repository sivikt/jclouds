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
package org.jclouds.profitbricks;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.profitbricks.compute.config.PBComputeServiceAdapterContextModule;
import org.jclouds.profitbricks.compute.config.PBComputeServiceContextModule;
import org.jclouds.profitbricks.config.PBHttpApiModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import java.net.URI;
import java.util.Properties;

/**
 * Implementation of {@link org.jclouds.apis.ApiMetadata} for
 * ProfitBricks's Cloud Compute based on SOAP API.
 *
 * @author Serj Sintsov
 */
public class PBSoapApiMetadata extends BaseHttpApiMetadata<PBApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public PBSoapApiMetadata() {
      this(new Builder());
   }

   protected PBSoapApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      return BaseHttpApiMetadata.defaultProperties();
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<PBApi, Builder> {

      protected Builder() {
          id("profitbricks-c2-api")
         .name("ProfitBricks Cloud Compute 2.0 SOAP API")
         .identityName("Username")
         .credentialName("Password")
         .documentation(URI.create("http://www.profitbricks.com/apidoc/APIDocumentation.html"))
         .defaultEndpoint("https://api.profitbricks.com/1.2")
         .version("1.2")
         .view(ComputeServiceContext.class)
         .defaultProperties(PBSoapApiMetadata.defaultProperties())
         .defaultModules(ImmutableSet.<Class<? extends Module>>of(
               PBComputeServiceContextModule.class,
               PBComputeServiceAdapterContextModule.class,
               PBHttpApiModule.class
         ));
      }

      @Override
      public PBSoapApiMetadata build() {
         return new PBSoapApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
