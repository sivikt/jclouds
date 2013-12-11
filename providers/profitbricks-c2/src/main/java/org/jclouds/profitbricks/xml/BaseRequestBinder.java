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

import com.google.common.base.Strings;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;

import javax.ws.rs.core.MediaType;

/**
 * Provides basic methods to map any object to XML.
 *
 * @author Serj Sintsov
 */
public abstract class BaseRequestBinder implements MapBinder {

   protected  <R extends HttpRequest> R createRequest(R fromRequest, String payload) {
      fromRequest.setPayload(payload);
      fromRequest.getPayload().getContentMetadata().setContentType(MediaType.TEXT_XML);
      return fromRequest;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      throw new UnsupportedOperationException("Use bind method with post parameters instead");
   }

   protected String addIfNotNull(String pattern, Object param) {
      return param == null ? "" : String.format(pattern, param);
   }

   protected String addIfNotEmpty(String pattern, String param) {
      return Strings.isNullOrEmpty(param) ? "" : String.format(pattern, param);
   }

   protected String justAdd(String pattern, String param) {
      return String.format(pattern, param);
   }

   protected String justAdd(String pattern, int param) {
      return String.format(pattern, param);
   }

   protected String justAdd(String pattern, boolean param) {
      return String.format(pattern, param);
   }

}
