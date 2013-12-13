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
package org.jclouds.profitbricks.features;

import com.google.common.collect.ImmutableList;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.profitbricks.xml.datacenters.GetAllDataCentersResponseHandler;
import org.jclouds.profitbricks.xml.datacenters.GetDataCenterResponseHandler;
import org.jclouds.reflect.Invocation;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.jclouds.reflect.Reflection2.method;
import static org.testng.Assert.assertNotNull;

/**
 * Integration test for {@link DataCenterApi}.
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "DataCenterApiTest")
public class DataCenterApiTest extends BasePBApiTest<ServerApi> {

   @Test
   public void getDataCenter() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
            method(DataCenterApi.class, "getDataCenter", String.class),
            ImmutableList.<Object>of("4aa7")
      );

      GeneratedHttpRequest request = processor.apply(invocation);
      assertNotNull(request);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getDataCenter><dataCenterId>4aa7</dataCenterId></ws:getDataCenter>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetDataCenterResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

   @Test
   public void listDataCenters() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(method(DataCenterApi.class, "listDataCenters"));

      GeneratedHttpRequest request = processor.apply(invocation);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getAllDataCenters/>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetAllDataCentersResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), null);

      checkHeaders(request);
   }

}
