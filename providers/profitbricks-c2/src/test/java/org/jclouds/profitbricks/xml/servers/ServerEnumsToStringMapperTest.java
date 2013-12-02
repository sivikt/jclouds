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

import org.jclouds.profitbricks.domain.Server;
import org.jclouds.profitbricks.xml.servers.ServerEnumsToStringMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test for {@code ServerEnumsToStringMapper}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "ServerEnumsToStringMapperTest")
public class ServerEnumsToStringMapperTest {

   private ServerEnumsToStringMapper mapper = new ServerEnumsToStringMapper();

   @Test
   public void testMapOsType() {
      assertEquals(mapper.mapOSType(null), "");
      assertEquals(mapper.mapOSType(Server.OSType.OTHER), "OTHER");
      assertEquals(mapper.mapOSType(Server.OSType.UNKNOWN), "UNKNOWN");
      assertEquals(mapper.mapOSType(Server.OSType.WINDOWS), "WINDOWS");
      assertEquals(mapper.mapOSType(Server.OSType.LINUX), "LINUX");
   }

   @Test
   public void testMapAvailabilityZone() {
      assertEquals(mapper.mapAvailabilityZone(null), "");
      assertEquals(mapper.mapAvailabilityZone(Server.AvailabilityZone.AUTO), "AUTO");
      assertEquals(mapper.mapAvailabilityZone(Server.AvailabilityZone.ZONE_1), "ZONE_1");
      assertEquals(mapper.mapAvailabilityZone(Server.AvailabilityZone.ZONE_2), "ZONE_2");
   }

}
