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
package org.jclouds.profitbricks.compute.features;

import org.jclouds.Fallbacks;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.profitbricks.features.ServerApi;
import org.jclouds.profitbricks.xml.GetAllServersResponseHandler;
import org.jclouds.reflect.Invocation;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.jclouds.reflect.Reflection2.method;

/**
 * Test for {@link org.jclouds.profitbricks.features.ServerApi}
 * 
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "ServersApiTest")
public class ServersApiTest extends BasePBApiTest<ServerApi> {

   @Test
   public void getAllServers() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(method(ServerApi.class, "getAllServers"));

      GeneratedHttpRequest request = processor.apply(invocation);

      request = (GeneratedHttpRequest) request.getFilters().get(0).filter(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getAllServers/>", "application/unknown", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetAllServersResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), Fallbacks.EmptyFluentIterableOnNotFoundOr404.class);

      checkHeaders(request);
      checkFilters(request);
   }

}
