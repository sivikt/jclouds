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
package org.jclouds.profitbricks.xml;

import com.google.common.base.Strings;
import org.jclouds.http.HttpRequest;
import org.jclouds.profitbricks.domain.Server;
import org.jclouds.rest.MapBinder;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create XML payload to send new server creation request.
 *
 * @author Serj Sintsov
 */
public class CreateServerRequestBinder implements MapBinder {

   private ServerEnumsToStringMapper serverEnumsMapper;

   @Inject
   public CreateServerRequestBinder(ServerEnumsToStringMapper serverEnumsMapper) {
      this.serverEnumsMapper = checkNotNull(serverEnumsMapper, "serverEnumsMapper");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkNotNull(request, "request");

      Object serverObj = postParams.get(PBApiRequestParameters.SERVER_ENTITY);
      checkNotNull(serverObj, "server");
      Server server = Server.class.cast(serverObj);

      return createRequest(request, generateRequestPayload(server));
   }

   private String generateRequestPayload(Server server) {
      return "<ws:createServer>" +
                "<request>" +
                   addIfNotEmpty("<dataCenterId>%s</dataCenterId>", server.getDataCenterId()) +
                   addIfNotEmpty("<serverName>%s</serverName>", server.getServerName()) +
                   justAdd("<cores>%s</cores>", server.getCores()) +
                   justAdd("<ram>%s</ram>", server.getRam()) +
                   justAdd("<internetAccess>%s</internetAccess>", server.isInternetAccess()) +
                   addIfNotEmpty("<osType>%s</osType>", serverEnumsMapper.mapOSType(server.getOsType())) +
                   addIfNotEmpty("<availabilityZone>%s</availabilityZone>", serverEnumsMapper.mapAvailabilityZone(server.getAvailabilityZone())) +
                "</request>" +
             "</ws:createServer>";
   }

   private String addIfNotEmpty(String pattern, String param) {
      return Strings.isNullOrEmpty(param) ? "" : String.format(pattern, param);
   }

   private String justAdd(String pattern, int param) {
      return String.format(pattern, param);
   }

   private String justAdd(String pattern, boolean param) {
      return String.format(pattern, param);
   }

   private <R extends HttpRequest> R createRequest(R fromRequest, String payload) {
      fromRequest.setPayload(payload);
      fromRequest.getPayload().getContentMetadata().setContentType(MediaType.TEXT_XML);
      return fromRequest;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      throw new UnsupportedOperationException("Use bind method with post parameters instead");
   }

}
