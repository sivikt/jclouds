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

import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.profitbricks.xml.servers.CreateServerResponseHandler;
import org.testng.annotations.Test;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link org.jclouds.profitbricks.xml.servers.CreateServerResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "CreateServerResponseHandlerTest")
public class CreateServerResponseHandlerTest extends BaseHandlerTest {

   @Test
   public void testHandlerResult() {
      InputStream is = getClass().getResourceAsStream("/servers/createServerResponse.xml");

      CreateServerResponseHandler handler = injector.getInstance(CreateServerResponseHandler.class);
      String actualServerId = factory.create(handler).parse(is);

      assertNotNull(actualServerId);
      assertEquals(actualServerId, "8eb5a0df-ddea-4a98-a628-8833dcc9309f");
   }

}
