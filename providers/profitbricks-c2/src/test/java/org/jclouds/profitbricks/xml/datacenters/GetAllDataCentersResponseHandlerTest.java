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
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

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

      List<String> expectedResult = expectedResult();

      GetAllDataCentersResponseHandler handler = injector.getInstance(GetAllDataCentersResponseHandler.class);
      Set<String> result = factory.create(handler).parse(is);

      assertNotNull(result);
      assertEquals(result.size(), 2);

      for (String expectedId : expectedResult)
         findInSet(result, expectedId);
   }

   private void findInSet(Set<String> src, String dataCenterId) {
      for (String dcId : src)
         if (dcId.equals(dataCenterId)) return;

      fail("dataCenterId=" + dataCenterId + " not found");
   }

   private List<String> expectedResult() {
      return Lists.newArrayList(
         "a2de7e7a-fb70-4eaf-95ce-70f3bc061121",
         "22222222-fb70-4eaf-95ce-20f3bc061222"
      );
   }

}
