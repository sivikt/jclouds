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

import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.Server;

import javax.inject.Inject;

/**
 * XML parser to handle success response on GetServer request.
 *
 * @author Serj Sintsov
 */
public class GetServerResponseHandler extends BaseFullServerInfoResponseHandler<Server> {

   @Inject
   public GetServerResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
   }

   @Override
   public Server getResult() {
      return currentServer;
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      if (currentServer == null)
         super.endElement(uri, name, qName);
   }

}
