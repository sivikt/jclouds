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
package org.jclouds.profitbricks;

import org.jclouds.profitbricks.features.DataCenterApi;
import org.jclouds.profitbricks.features.FirewallApi;
import org.jclouds.profitbricks.features.ServerApi;
import org.jclouds.rest.annotations.*;

import java.io.Closeable;

/**
 * Provides access to ProfitBricks's Cloud Compute specific features.
 * <p/>
 * As for the current moment ProfitBricks has only SOAP based API and
 * REST is coming soon. So, by the default all PB communications are
 * delegated to SOAP endpoints.
 *
 * @see <a href="http://www.profitbricks.com/apidoc/APIDocumentation.html" />
 * @see <a href="https://api.profitbricks.com/1.2" />
 * @see <a href="https://api.profitbricks.com/1.2/wsdl" />
 *
 * @author Serj Sintsov
 */
public interface PBApi extends Closeable {

   @Delegate
   DataCenterApi dataCenterApi();

   @Delegate
   ServerApi serversApi();

   @Delegate
   FirewallApi firewallApi();

}
