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

import org.jclouds.date.DateCodecFactory;

import javax.inject.Inject;

/**
 * XML parser to handle CreateServer success response.
 *
 * @author Serj Sintsov
 */
public class CreateServerResponseHandler extends BasePBResponseHandler<String> {

   private boolean isDone;
   private String serverId;

   @Inject
   public CreateServerResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
   }

   @Override
   public String getResult() {
      return serverId;
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      if (isDone) return;
      if (qName.equals("serverId")) {
         serverId = trimAndGetTagStrValue();
         isDone = true;
      }
      clearTextBuffer();
   }

}
