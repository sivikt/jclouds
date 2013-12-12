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
import org.jclouds.profitbricks.xml.BaseRequestBinder;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;

import javax.inject.Inject;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create XML payload for operation
 * {@link org.jclouds.profitbricks.features.ServerApi#createServer(org.jclouds.profitbricks.domain.specs.ServerCreationSpec)}.
 *
 * @author Serj Sintsov
 */
public class CreateServerRequestBinder extends BaseRequestBinder {

   private EnumsToRequestParamMapper enumsToRequestParam;

   @Inject
   public CreateServerRequestBinder(EnumsToRequestParamMapper enumsToRequestParam) {
      this.enumsToRequestParam = checkNotNull(enumsToRequestParam, "enumsToRequestParam");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkNotNull(request, "request");

      Object serverSpecObj = postParams.get(PBApiRequestParameters.SERVER_SPECIFICATION);
      checkNotNull(serverSpecObj, "server specification");
      ServerCreationSpec serverSpec = ServerCreationSpec.class.cast(serverSpecObj);

      return createRequest(request, mapToXml(serverSpec));
   }

   private String mapToXml(ServerCreationSpec serverSpec) {
      return "<ws:createServer>" +
                "<request>" +
                   addIfNotEmpty("<dataCenterId>%s</dataCenterId>", serverSpec.getDataCenterId()) +
                   addIfNotEmpty("<serverName>%s</serverName>", serverSpec.getServerName()) +
                   justAdd("<cores>%s</cores>", serverSpec.getCores()) +
                   justAdd("<ram>%s</ram>", serverSpec.getRam()) +
                   justAdd("<internetAccess>%s</internetAccess>", serverSpec.isInternetAccess()) +
                   addIfNotEmpty("<bootFromImageId>%s</bootFromImageId>", serverSpec.getBootFromImageId()) +
                   addIfNotEmpty("<osType>%s</osType>", enumsToRequestParam.mapOSType(serverSpec.getOsType())) +
                   addIfNotEmpty("<availabilityZone>%s</availabilityZone>", enumsToRequestParam.mapAvailabilityZone(serverSpec.getAvailabilityZone())) +
                "</request>" +
             "</ws:createServer>";
   }

}
