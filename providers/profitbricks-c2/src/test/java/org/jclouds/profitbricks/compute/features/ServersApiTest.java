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

import com.google.common.collect.ImmutableList;
import org.jclouds.Fallbacks;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.profitbricks.features.ServerApi;
import org.jclouds.profitbricks.xml.GetAllServersResponseHandler;
import org.jclouds.profitbricks.xml.GetServerResponseHandler;
import org.jclouds.reflect.Invocation;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

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

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getAllServers/>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetAllServersResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), Fallbacks.EmptySetOnNotFoundOr404.class);

      checkHeaders(request);
   }

   @Test
   public void getServer() throws SecurityException, NoSuchMethodException, IOException {
      Invocation invocation = Invocation.create(
         method(ServerApi.class, "getServer", String.class),
         ImmutableList.<Object>of("93981076-2511-4aa7-82c0-1e4df0d1737f")
      );

      GeneratedHttpRequest request = processor.apply(invocation);
      assertNotNull(request);

      checkFilters(request);

      assertRequestLineEquals(request, "POST https://api.profitbricks.com/1.2 HTTP/1.1");
      assertPayloadEquals(request, "<ws:getServer><serverId>93981076-2511-4aa7-82c0-1e4df0d1737f</serverId></ws:getServer>", "text/xml", false);
      assertResponseParserClassEquals(invocation.getInvokable(), request, ParseSax.class);
      assertSaxResponseParserClassEquals(invocation.getInvokable(), GetServerResponseHandler.class);
      assertFallbackClassEquals(invocation.getInvokable(), Fallbacks.NullOnNotFoundOr404.class);

      checkHeaders(request);
   }

}
