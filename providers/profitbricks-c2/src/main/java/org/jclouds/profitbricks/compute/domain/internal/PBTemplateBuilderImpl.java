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
package org.jclouds.profitbricks.compute.domain.internal;

import com.google.common.base.Supplier;
import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.*;
import org.jclouds.compute.domain.internal.TemplateBuilderImpl;
import org.jclouds.compute.domain.internal.TemplateImpl;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Set;

/**
 * ProfitBricks' {@link TemplateBuilder} implementation.
 *
 * TODO investigate and leave/remove
 *
 * @author Serj Sintsov
 */
public class PBTemplateBuilderImpl extends TemplateBuilderImpl {

   private Hardware hardware;
   private Image image;

   @Inject
   protected PBTemplateBuilderImpl(@Memoized Supplier<Set<? extends Location>> locations,
                                   @Memoized Supplier<Set<? extends Image>> images,
                                   @Memoized Supplier<Set<? extends Hardware>> hardwares,
                                   Supplier<Location> defaultLocation,
                                   @Named("DEFAULT") Provider<TemplateOptions> optionsProvider,
                                   @Named("DEFAULT") Provider<TemplateBuilder> defaultTemplateProvider) {
      super(locations, images, hardwares, defaultLocation, optionsProvider, defaultTemplateProvider);
   }

   @Override
   public Template build() {
      chooseImage();
      chooseHardware();
      chooseLocation();
      chooseOptions();

      return new TemplateImpl(image, hardware, location, options);
   }

   protected void chooseHardware() {
      // build fake hardware
      hardware = new HardwareBuilder()
            .id("fake")
            .ram(0)
            .processor(new Processor(0, 0))
            .build();
   }

   protected void chooseLocation() {
      if (location == null)
         location = defaultLocation.get();
   }

   protected void chooseOptions() {
      if (options == null)
         options = optionsProvider.get();
   }

   protected void chooseImage() {
      // build fake image
      image = new ImageBuilder()
            .id("fake")
            .operatingSystem(new OperatingSystem.Builder().family(OsFamily.LINUX).description(OsFamily.LINUX.value()).build())
            .status(Image.Status.AVAILABLE)
            .build();
   }

   @Override
   public TemplateBuilder fromHardware(Hardware hardware) {
      this.hardware = hardware;
      return this;
   }

   @Override
   public TemplateBuilder fromImage(Image image) {
      this.image = image;
      return this;
   }

}
