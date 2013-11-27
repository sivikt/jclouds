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

import com.google.inject.Module;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.profitbricks.PBSoapApiMetadata;
import org.jclouds.profitbricks.config.PBHttpApiModule;
import org.jclouds.profitbricks.filters.PBSoapMessageEnvelope;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.internal.BaseAsyncApiTest;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Base ProfitBricks API test
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit")
public abstract class BasePBApiTest<T> extends BaseAsyncApiTest<T> {

   protected final String HTTP_HEADERS = "Authorization: Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==\n" +
                                         "Host: api.profitbricks.com\n";

   @ConfiguresHttpApi
   public static class BasePBApiTestModule extends PBHttpApiModule { }

   @Override
   protected Module createModule() {
      return new BasePBApiTestModule();
   }

   @Override
   protected ApiMetadata createApiMetadata() {
      return new PBSoapApiMetadata();
   }

   @Override
   protected void checkFilters(HttpRequest request) {
      assertEquals(request.getFilters().size(), 2);
      assertEquals(request.getFilters().get(0).getClass(), BasicAuthentication.class);
      assertEquals(request.getFilters().get(1).getClass(), PBSoapMessageEnvelope.class);
   }

   protected void checkHeaders(GeneratedHttpRequest request) {
      assertNonPayloadHeadersEqual(request, HTTP_HEADERS);
   }

}
