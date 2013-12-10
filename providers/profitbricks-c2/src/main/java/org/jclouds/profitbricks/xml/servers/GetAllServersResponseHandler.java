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

import com.google.common.collect.Sets;
import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.Server;

import javax.inject.Inject;

import java.util.Set;

/**
 * XML parser to handle success response on GetAllServers request.
 *
 * @author Serj Sintsov
 */
public class GetAllServersResponseHandler extends BaseFullServerInfoResponseHandler<Set<Server>> {

   private Set<Server> servers;

   @Inject
   public GetAllServersResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
      servers = Sets.newHashSet();
   }

   @Override
   public Set<Server> getResult() {
      return servers;
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      if (currentServer == null)
         super.endElement(uri, name, qName);

      if (currentServer != null) {
         servers.add(currentServer);
         currentServer = null;
      }
   }

}
