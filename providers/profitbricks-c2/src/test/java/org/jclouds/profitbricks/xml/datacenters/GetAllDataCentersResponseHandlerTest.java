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

import com.google.common.collect.Lists;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.domain.DataCenter;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link GetAllDataCentersResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "GetAllDataCentersResponseHandlerTest")
public class GetAllDataCentersResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/datacenters/getAllDataCentersResponse.xml");

      List<DataCenter> expectedResult = expectedResult();

      GetAllDataCentersResponseHandler handler = injector.getInstance(GetAllDataCentersResponseHandler.class);
      Set<DataCenter> result = factory.create(handler).parse(is);

      assertNotNull(result);
      assertEquals(result.size(), 3);

      for (DataCenter expectedDC : expectedResult) {
         DataCenter actualDC = findInSet(result, expectedDC.getDataCenterId());

         assertNotNull(actualDC, errForDC(expectedDC));
         assertEquals(actualDC.getDataCenterName(), expectedDC.getDataCenterName(), errForDC(expectedDC));
         assertEquals(actualDC.getProvisioningState(), expectedDC.getProvisioningState(), errForDC(expectedDC));
         assertEquals(actualDC.getRegion(), expectedDC.getRegion(), errForDC(expectedDC));
      }
   }

   private String errForDC(DataCenter dc) {
      return "dataCenterId=" + dc.getDataCenterId();
   }

   private DataCenter findInSet(Set<DataCenter> src, String dataCenterId) {
      for (DataCenter dc : src)
         if (dc.getDataCenterId().equals(dataCenterId)) return dc;

      return null;
   }

   private List<DataCenter> expectedResult() {
      return Lists.newArrayList(
            DataCenter.describingBuilder()
                  .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                  .dataCenterName("DC_1")
                  .provisioningState(ProvisioningState.AVAILABLE)
                  .region(DataCenter.DataCenterRegion.NORTH_AMERICA)
                  .build(),

            DataCenter.describingBuilder()
                  .dataCenterId("89046edb-2a50-4d0f-a153-6576ee7d22a7")
                  .dataCenterName("DC_2")
                  .provisioningState(ProvisioningState.INACTIVE)
                  .region(DataCenter.DataCenterRegion.EUROPE)
                  .build(),

            DataCenter.describingBuilder()
                  .dataCenterId("79046edb-2a50-4d0f-a153-6576ee7d22a6")
                  .dataCenterName("DC_3")
                  .provisioningState(ProvisioningState.AVAILABLE)
                  .region(DataCenter.DataCenterRegion.NORTH_AMERICA)
                  .build()

      );
   }

}
