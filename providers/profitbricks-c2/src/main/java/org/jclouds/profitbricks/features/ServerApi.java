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

import static org.jclouds.Fallbacks.EmptyFluentIterableOnNotFoundOr404;

import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.filters.PBSoapMessageEnvelope;
import org.jclouds.profitbricks.xml.GetAllServersResponseHandler;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SinceApiVersion;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.XMLResponseParser;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.Payload;

import javax.inject.Named;
import javax.ws.rs.POST;
import java.util.Set;

/**
 * Provides synchronous access to ProfitBricks's Server Operations API.
 *
 * @author Serj Sintsov
 */
@SinceApiVersion("1.2")
@RequestFilters({BasicAuthentication.class, PBSoapMessageEnvelope.class})
@VirtualHost
public interface ServerApi {

   /**
    * Returns information about all virtual server, such as
    * configuration, provisioning status, power status, etc.
    *
    * @return servers in your cloud data centers or empty if there are none
    */
   @POST
   @Named("GetAllServers")
   @Payload("<ws:getAllServers/>")
   @XMLResponseParser(GetAllServersResponseHandler.class)
   @Fallback(EmptyFluentIterableOnNotFoundOr404.class)
   Set<Server> getAllServers();

}
