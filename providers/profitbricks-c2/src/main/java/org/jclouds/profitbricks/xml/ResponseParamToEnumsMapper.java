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
package org.jclouds.profitbricks.xml;

import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.ProvisioningState;

import javax.inject.Singleton;

/**
 * Maps strings to ProfitBricks' specific enums. Useful in response handlers.
 *
 * @author Serj Sintsov
 */
@Singleton
public class ResponseParamToEnumsMapper {

   public IpProtocol toIpProtocol(String s) {
      if ("ANY".equals(s))
         return IpProtocol.ALL;

      return s == null ? null : IpProtocol.fromValue(s);
   }

   public ProvisioningState toProvState(String s) {
      return s == null ? null : ProvisioningState.fromValue(s);
   }

}
