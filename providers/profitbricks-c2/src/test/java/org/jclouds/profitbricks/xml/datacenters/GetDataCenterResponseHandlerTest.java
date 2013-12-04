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
package org.jclouds.profitbricks.xml.datacenters;

import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.domain.DataCenter;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Regions;
import org.testng.annotations.Test;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link GetDataCenterResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetDataCenterResponseHandlerTest")
public class GetDataCenterResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/datacenters/getDataCenterResponse.xml");

      DataCenter expectedResult = DataCenter.describingBuilder()
            .dataCenterId("a2de7e7a-fb70-4eaf-95ce-70f3bc061121")
            .dataCenterName("Unnamed Data Center")
            .provisioningState(ProvisioningState.AVAILABLE)
            .region(Regions.EUROPE)
            .build();

      GetDataCenterResponseHandler handler = injector.getInstance(GetDataCenterResponseHandler.class);
      DataCenter result = factory.create(handler).parse(is);

      assertNotNull(result);
      assertEquals(result.getDataCenterId(), expectedResult.getDataCenterId());
      assertEquals(result.getDataCenterName(), expectedResult.getDataCenterName());
      assertEquals(result.getProvisioningState(), expectedResult.getProvisioningState());
      assertEquals(result.getRegion(), expectedResult.getRegion());
   }

}
