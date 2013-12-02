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
package org.jclouds.profitbricks.xml.datacenters;

import com.google.common.collect.Sets;
import org.jclouds.date.DateCodecFactory;
import org.jclouds.profitbricks.domain.DataCenter;

import javax.inject.Inject;
import java.util.Set;

/**
 * XML parser to handle success response on GetAllServers request.
 *
 * @author Serj Sintsov
 */
public class GetAllDataCentersResponseHandler extends BaseFullDataCenterInfoResponseHandler<Set<DataCenter>> {

   private Set<DataCenter> dataCenters;

   @Inject
   public GetAllDataCentersResponseHandler(DateCodecFactory dateCodecFactory) {
      super(dateCodecFactory);
      dataCenters = Sets.newHashSet();
   }

   @Override
   public Set<DataCenter> getResult() {
      return dataCenters;
   }

   @Override
   public void endElement(String uri, String name, String qName) {
      setServerInfoOnEndElementEvent(qName);
      if (qName.equals("return")) {
         dataCenters.add(describingBuilder.build());
         describingBuilder = DataCenter.describingBuilder();
      }
      clearTextBuffer();
   }

}
