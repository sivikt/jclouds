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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.VisibleForTesting;
import org.jclouds.date.DateCodec;
import org.jclouds.date.DateCodecFactory;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.javax.annotation.Nullable;

import java.util.Date;

/**
 * Base ProfitBricks response handler with some useful methods.
 *
 * @author Serj Sintsov
 */
public abstract class BasePBResponseHandler<T> extends ParseSax.HandlerForGeneratedRequestWithResult<T> {

   protected final DateCodec dateCodec;

   private StringBuilder textBuf;

   public BasePBResponseHandler(DateCodecFactory dateCodecFactory) {
      dateCodec = checkNotNull(checkNotNull(dateCodecFactory, "dateCodecFactory").iso8601(), "iso8601 date codec");
      textBuf = new StringBuilder();
   }

   /**
    * Uses {@link #trimAndGetTagStrValue()}
    */
   @VisibleForTesting
   protected final Date textBufferToIso8601Date() {
      return dateCodec.toDate(trimAndGetTagStrValue());
   }

   @VisibleForTesting
   protected String textBufferValue() {
      return textBuf.toString();
   }

   /**
    * Uses {@link #textBufferValue()}
    */
   @VisibleForTesting
   protected String trimAndGetTagStrValue() {
      return textBufferValue().trim();
   }

   /**
    * Uses {@link #trimAndGetTagStrValue()}
    */
   @VisibleForTesting
   protected int textBufferToIntValue() {
      return Integer.parseInt(trimAndGetTagStrValue());
   }

   /**
    * Uses {@link #trimAndGetTagStrValue()}
    */
   @VisibleForTesting
   @Nullable
   protected Integer textBufferToIntegerValue() {
      String s = trimAndGetTagStrValue();
      return s.length() > 0 ? Integer.valueOf(s) : null;
   }

   /**
    * Uses {@link #trimAndGetTagStrValue()}
    */
   @VisibleForTesting
   protected boolean textBufferToBoolValue() {
      return Boolean.parseBoolean(trimAndGetTagStrValue());
   }

   @VisibleForTesting
   protected void clearTextBuffer() {
      textBuf = new StringBuilder();
   }

   @Override
   public void characters(char ch[], int start, int length) {
      textBuf.append(ch, start, length);
   }

}
