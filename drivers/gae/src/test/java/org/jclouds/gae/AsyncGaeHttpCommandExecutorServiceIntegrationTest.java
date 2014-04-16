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
package org.jclouds.gae;

import java.io.IOException;
import java.util.Properties;

import org.jclouds.concurrent.SingleThreaded;
import org.jclouds.concurrent.config.ConfiguresExecutorService;
import org.jclouds.gae.config.GoogleAppEngineConfigurationModule;
import org.jclouds.http.BaseHttpCommandExecutorServiceIntegrationTest;
import org.jclouds.http.HttpCommandExecutorService;
import org.jclouds.http.config.ConfiguresHttpCommandExecutorService;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 * Integration test for the URLFetchService
 * 
 * @author Adrian Cole
 */
@Test
public class AsyncGaeHttpCommandExecutorServiceIntegrationTest extends BaseHttpCommandExecutorServiceIntegrationTest {
   
   @BeforeMethod
   public void setupApiProxy() {
      LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalURLFetchServiceTestConfig());
      helper.setUp();
   }

   @Override
   public void testPostAsInputStream() {
      throw new SkipException("streams aren't supported");
   }
   
   @Override
   public void testPostAsInputStreamDoesNotRetryOnFailure() throws Exception {
      throw new SkipException("streams aren't supported");
   }
   
   @Override
   public void testGetBigFile()  {
      throw new SkipException("test data is too big for GAE");
   }

   @Override
   public void testUploadBigFile() throws IOException {
      throw new SkipException("test data is too big for GAE");
   }

   protected Module createConnectionModule() {
      setupApiProxy();
      return new AsyncGoogleAppEngineConfigurationModule();
   }

   @ConfiguresHttpCommandExecutorService
   @ConfiguresExecutorService
   @SingleThreaded
   public class AsyncGoogleAppEngineConfigurationModule extends GoogleAppEngineConfigurationModule {

      public AsyncGoogleAppEngineConfigurationModule() {
         super();
      }

      protected HttpCommandExecutorService providerHttpCommandExecutorService(Injector injector) {
         return injector.getInstance(AsyncGaeHttpCommandExecutorService.class);
      }

   }

   @Override
   protected void addOverrideProperties(Properties props) {
      
   }

}
