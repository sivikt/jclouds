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
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test for {@link EnumsToRequestParamMapper}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "EnumsToRequestParamMapperTest")
public class EnumsToRequestParamMapperTest {

   private EnumsToRequestParamMapper mapper = new EnumsToRequestParamMapper();

   @Test
   public void testMapOsType() {
      assertEquals(mapper.fromOSType(null), "");
      assertEquals(mapper.fromOSType(OSType.OTHER), "OTHER");
      assertEquals(mapper.fromOSType(OSType.UNKNOWN), "UNKNOWN");
      assertEquals(mapper.fromOSType(OSType.WINDOWS), "WINDOWS");
      assertEquals(mapper.fromOSType(OSType.LINUX), "LINUX");
   }

   @Test
   public void testMapAvailabilityZone() {
      assertEquals(mapper.fromAvailabilityZone(null), "");
      assertEquals(mapper.fromAvailabilityZone(AvailabilityZone.AUTO), "AUTO");
      assertEquals(mapper.fromAvailabilityZone(AvailabilityZone.ZONE_1), "ZONE_1");
      assertEquals(mapper.fromAvailabilityZone(AvailabilityZone.ZONE_2), "ZONE_2");
   }

   @Test
   public void testMapIpProtocol() {
      assertEquals(mapper.fromIpProtocol(null), "");
      assertEquals(mapper.fromIpProtocol(IpProtocol.ALL), "ANY");
      assertEquals(mapper.fromIpProtocol(IpProtocol.TCP), "TCP");
      assertEquals(mapper.fromIpProtocol(IpProtocol.UDP), "UDP");
      assertEquals(mapper.fromIpProtocol(IpProtocol.ICMP), "ICMP");
   }

}
