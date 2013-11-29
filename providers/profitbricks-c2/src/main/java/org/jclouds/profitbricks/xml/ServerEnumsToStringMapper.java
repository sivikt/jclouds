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

import org.jclouds.profitbricks.domain.Server;

import javax.inject.Singleton;

/**
 * Maps {@link Server} specific enums to strings. Useful in the requests binders.
 *
 * @author Serj Sintsov
 */
@Singleton
public class ServerEnumsToStringMapper {

   public String mapOSType(Server.OSType osType) {
      return osType == null ? "" : osType.value();
   }

   public String mapAvailabilityZone(Server.AvailabilityZone zone) {
      return zone == null ? "" : zone.value();
   }

}
