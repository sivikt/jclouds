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
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test for {@link org.jclouds.profitbricks.xml.EnumsToRequestParamMapper}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "ResponseParamToEnumsMapperTest")
public class ResponseParamToEnumsMapperTest {

   private ResponseParamToEnumsMapper mapper = new ResponseParamToEnumsMapper();

   @Test
   public void testToIpProtocol() {
      assertEquals(mapper.toIpProtocol(null), null);
      assertEquals(mapper.toIpProtocol("TCP"), IpProtocol.TCP);
      assertEquals(mapper.toIpProtocol("UDP"), IpProtocol.UDP);
      assertEquals(mapper.toIpProtocol("ICMP"), IpProtocol.ICMP);
      assertEquals(mapper.toIpProtocol("ANY"), IpProtocol.ALL);
      assertEquals(mapper.toIpProtocol("HTTP"), IpProtocol.UNRECOGNIZED);
   }

   @Test
   public void testToProvState() {
      assertEquals(mapper.toProvState(null), null);
      assertEquals(mapper.toProvState("AVAILABLE"), ProvisioningState.AVAILABLE);
      assertEquals(mapper.toProvState("DELETED"), ProvisioningState.DELETED);
      assertEquals(mapper.toProvState("ERROR"), ProvisioningState.ERROR);
      assertEquals(mapper.toProvState("INACTIVE"), ProvisioningState.INACTIVE);
      assertEquals(mapper.toProvState("INPROCESS"), ProvisioningState.INPROCESS);
   }

}
