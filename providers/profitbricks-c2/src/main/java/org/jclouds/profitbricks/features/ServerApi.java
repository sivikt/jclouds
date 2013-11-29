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

import static org.jclouds.Fallbacks.EmptySetOnNotFoundOr404;
import static org.jclouds.Fallbacks.NullOnNotFoundOr404;

import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.filters.PBSoapMessageEnvelope;
import org.jclouds.profitbricks.xml.CreateServerRequestBinder;
import org.jclouds.profitbricks.xml.CreateServerResponseHandler;
import org.jclouds.profitbricks.xml.GetAllServersResponseHandler;
import org.jclouds.profitbricks.xml.GetServerResponseHandler;
import static org.jclouds.profitbricks.xml.PBApiRequestParameters.SERVER_ENTITY;
import org.jclouds.rest.annotations.Payload;
import org.jclouds.rest.annotations.SinceApiVersion;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.XMLResponseParser;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.MapBinder;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Provides synchronous access to ProfitBricks's Server Operations API.
 *
 * TODO handle request/response errors
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
   @POST // TODO live and expect test
   @Named("GetAllServers")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @Payload("<ws:getAllServers/>")
   @XMLResponseParser(GetAllServersResponseHandler.class)
   @Fallback(EmptySetOnNotFoundOr404.class)
   Set<Server> getAllServers();


   /**
    * Returns information about a virtual server,
    * such as configuration, provisioning status, power status, etc.
    *
    * @param serverId server identificator
    * @return an existing {@link Server} or {@code null}
    */
   @POST // TODO live and expect test
   @Named("GetServer")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @Payload("<ws:getServer><serverId>{id}</serverId></ws:getServer>")
   @XMLResponseParser(GetServerResponseHandler.class)
   @Fallback(NullOnNotFoundOr404.class)
   Server getServer(@PayloadParam("id") String serverId);

   /**
    * Creates a Virtual Server.
    *
    * @param server server entity to create
    * @return server identifier or {@code null} if creation is failed
    */
   @POST // TODO live and expect test
   @Named("CreateServer")
   @Consumes(MediaType.TEXT_XML)
   @Produces(MediaType.TEXT_XML)
   @MapBinder(CreateServerRequestBinder.class)
   @XMLResponseParser(CreateServerResponseHandler.class)
   @Fallback(NullOnNotFoundOr404.class)
   String createServer(@PayloadParam(SERVER_ENTITY) Server server);

}
