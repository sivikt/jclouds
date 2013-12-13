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
import com.google.common.collect.Iterables;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;
import org.jclouds.profitbricks.domain.DataCenter;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Transforms a ProfitBricks {@link DataCenter} into a generic {@code Location} object.
 *    TODO test it or delete it
 * @author Serj Sintsov
 */
public class DataCenterToLocation implements Function<DataCenter, Location> {

   private JustProvider provider;

   @Inject
   public DataCenterToLocation(JustProvider provider) {
      this.provider = checkNotNull(provider, "provider");
   }

   @Override
   public Location apply(DataCenter dataCenter) {
      return new LocationBuilder().scope(LocationScope.ZONE)
            .id(dataCenter.getId())
            .description(dataCenter.getDataCenterName())
            .parent(Iterables.getOnlyElement(provider.get()))
            .build();
   }

}
