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

import com.google.common.base.Strings;
import org.jclouds.profitbricks.domain.options.ServerCreationSpec;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Maps {@link ServerCreationSpec} to a valid server creation request XML.
 * @author Serj Sintsov
 */
@Singleton
public class ServerCreationSpecToXmlMapper {

   private ServerEnumsToStringMapper serverEnumsMapper;

   @Inject
   public ServerCreationSpecToXmlMapper(ServerEnumsToStringMapper serverEnumsMapper) {
      this.serverEnumsMapper = checkNotNull(serverEnumsMapper, "serverEnumsMapper");
   }

   public String map(ServerCreationSpec serverSpec) {
      return "<ws:createServer>" +
                "<request>" +
                   addIfNotEmpty("<dataCenterId>%s</dataCenterId>", serverSpec.getDataCenterId()) +
                   addIfNotEmpty("<serverName>%s</serverName>", serverSpec.getServerName()) +
                   justAdd("<cores>%s</cores>", serverSpec.getCores()) +
                   justAdd("<ram>%s</ram>", serverSpec.getRam()) +
                   justAdd("<internetAccess>%s</internetAccess>", serverSpec.isInternetAccess()) +
                   addIfNotEmpty("<bootFromImageId>%s</bootFromImageId>", serverSpec.getBootFromImageId()) +
                   addIfNotEmpty("<osType>%s</osType>", serverEnumsMapper.mapOSType(serverSpec.getOsType())) +
                   addIfNotEmpty("<availabilityZone>%s</availabilityZone>", serverEnumsMapper.mapAvailabilityZone(serverSpec.getAvailabilityZone())) +
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

}
