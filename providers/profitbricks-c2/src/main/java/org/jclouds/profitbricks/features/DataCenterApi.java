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
package org.jclouds.profitbricks.features;

import org.jclouds.Fallbacks;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.profitbricks.domain.DataCenter;
import org.jclouds.profitbricks.filters.PBSoapMessageEnvelope;
import org.jclouds.profitbricks.xml.servers.GetAllServersResponseHandler;
import org.jclouds.rest.annotations.*;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Provides synchronous access to ProfitBricks's Virtual Data Center Operations API.
 *
 * @author Serj Sintsov
 */
@SinceApiVersion("1.2")
@RequestFilters({BasicAuthentication.class, PBSoapMessageEnvelope.class})
@VirtualHost
public interface DataCenterApi {

   /**
    * Returns information about all available data centers.
    * @return set of {@link DataCenter} instances or empty set if there are none
    */
   @POST // TODO live and expect test
   @Named("GetAllDataCenters")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @Payload("<ws:getAllDataCenters/>")
   @XMLResponseParser(GetAllServersResponseHandler.class)
   @Fallback(Fallbacks.EmptySetOnNotFoundOr404.class)
   Set<DataCenter> getAllDataCenters();

}
