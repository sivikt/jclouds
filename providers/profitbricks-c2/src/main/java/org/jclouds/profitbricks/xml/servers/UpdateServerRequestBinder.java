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
import org.jclouds.profitbricks.domain.specs.ServerUpdatingSpec;
import org.jclouds.profitbricks.xml.BaseRequestBinder;
import org.jclouds.profitbricks.xml.EnumsToRequestParamMapper;
import org.jclouds.profitbricks.xml.PBApiRequestParameters;

import javax.inject.Inject;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Create XML payload for
 * {@link org.jclouds.profitbricks.features.ServerApi#updateServer(String, ServerUpdatingSpec)} operation.
 *
 * @author Serj Sintsov
 */
public class UpdateServerRequestBinder extends BaseRequestBinder {

   private EnumsToRequestParamMapper enumsToRequestParam;

   @Inject
   public UpdateServerRequestBinder(EnumsToRequestParamMapper enumsToRequestParam) {
      this.enumsToRequestParam = checkNotNull(enumsToRequestParam, "enumsToRequestParam");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkNotNull(request, "request");

      Object serverSpecObj = postParams.get(PBApiRequestParameters.SERVER_SPECIFICATION);
      Object serverIdObj = postParams.get(PBApiRequestParameters.SERVER_ID);

      checkNotNull(serverSpecObj, "server specification");
      ServerUpdatingSpec serverSpec = ServerUpdatingSpec.class.cast(serverSpecObj);

      checkNotNull(serverIdObj, "serverId");
      String serverId = String.class.cast(serverIdObj);

      return createRequest(request, mapToXml(serverId, serverSpec));
   }

   private String mapToXml(String serverId, ServerUpdatingSpec serverSpec) {
      return "<ws:updateServer>" +
                "<request>" +
                   justAdd("<serverId>%s</serverId>", serverId) +
                   addIfNotEmpty("<serverName>%s</serverName>", serverSpec.getServerName()) +
                   justAdd("<cores>%s</cores>", serverSpec.getCores()) +
                   justAdd("<ram>%s</ram>", serverSpec.getRam()) +
                   addIfNotEmpty("<bootFromImageId>%s</bootFromImageId>", serverSpec.getBootFromImageId()) +
                   addIfNotEmpty("<bootFromStorageId>%s</bootFromStorageId>", serverSpec.getBootFromStorageId()) +
                   addIfNotEmpty("<osType>%s</osType>", enumsToRequestParam.fromOSType(serverSpec.getOsType())) +
                   addIfNotEmpty("<availabilityZone>%s</availabilityZone>", enumsToRequestParam.fromAvailabilityZone(serverSpec.getAvailabilityZone())) +
                "</request>" +
             "</ws:updateServer>";
   }

}
