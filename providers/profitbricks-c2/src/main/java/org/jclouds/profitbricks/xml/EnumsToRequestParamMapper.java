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

import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;

import javax.inject.Singleton;

/**
 * Maps ProfitBricks' specific enums to strings. Useful in requests binders.
 *
 * @author Serj Sintsov
 */
@Singleton
public class EnumsToRequestParamMapper {

   public String fromOSType(OSType osType) {
      return osType == null ? "" : osType.value();
   }

   public String fromAvailabilityZone(AvailabilityZone zone) {
      return zone == null ? "" : zone.value();
   }

   public String fromIpProtocol(IpProtocol protocol) {
      if (protocol == IpProtocol.ALL)
         return "ANY";
      return protocol == null ? "" : protocol.name();
   }

}
