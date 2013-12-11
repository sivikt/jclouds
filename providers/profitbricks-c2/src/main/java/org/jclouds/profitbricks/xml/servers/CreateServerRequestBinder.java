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
package org.jclouds.profitbricks.xml.servers;

import org.jclouds.http.HttpRequest;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;
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

   private ServerCreationSpecToXmlMapper specToXmlMapper;

   @Inject
   public CreateServerRequestBinder(ServerCreationSpecToXmlMapper specToRequestXmlMapper) {
      this.specToXmlMapper = checkNotNull(specToRequestXmlMapper, "specToXmlMapper");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkNotNull(request, "request");

      Object serverSpecObj = postParams.get(PBApiRequestParameters.SERVER_SPECIFICATION);
      checkNotNull(serverSpecObj, "server specification");
      ServerCreationSpec serverSpec = ServerCreationSpec.class.cast(serverSpecObj);

      return createRequest(request, specToXmlMapper.map(serverSpec));
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
