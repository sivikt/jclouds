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

import org.easymock.EasyMock;
import org.jclouds.date.DateCodec;
import org.jclouds.date.DateCodecFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test for {@link BasePBResponseHandler}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "BasePBResponseHandlerTest")
public class BasePBResponseHandlerTest {

   private BasePBResponseHandler handler;

   private Date now;

   @BeforeMethod
   public void setUp() {
      DateCodecFactory dateCodecFactory = EasyMock.createMock(DateCodecFactory.class);
      DateCodec dateCodec = EasyMock.createMock(DateCodec.class);
      now = new Date();

      expect(dateCodec.toDate("2013")).andStubReturn(now);
      expect(dateCodecFactory.iso8601()).andStubReturn(dateCodec);
      replay(dateCodec, dateCodecFactory);

      handler = new BasePBResponseHandlerImpl(dateCodecFactory);
   }

   @Test
   public void testTextBufferValue() {
      handler.characters(new char[] {' ', 'v', 'a', 'l', 'u', 'e', ' '}, 0, 7);
      assertEquals(handler.textBufferValue(), " value ");

      handler.clearTextBuffer();
      assertEquals(handler.textBufferValue(), "");
   }

   @Test
   public void testTrimTextBufferValue() {
      handler.characters(new char[] {' ', 'v', 'a', 'l', 'u', 'e', ' '}, 0, 6);
      assertEquals(handler.trimAndGetTagStrValue(), "value");
   }

   @Test
   public void testTextBufferToIntValue() {
      handler.characters(new char[] {' ', '1', '2', '3', '4', '5', ' '}, 1, 2);
      assertEquals(handler.textBufferToIntValue(), 12);
   }

   @Test
   public void testTextBufferToBoolValue() {
      handler.characters(new char[] {' ', 't', 'r', 'u', 'e', '5', ' '}, 1, 4);
      assertTrue(handler.textBufferToBoolValue());

      handler.characters(new char[]{' ', 'F', 'A', 'L', 'S', 'E', ' '}, 1, 5);
      assertFalse(handler.textBufferToBoolValue());
   }

   @Test
   public void testTextBufferToDate() {
      handler.characters(new char[]{' ', '2', '0', '1', '3', ' '}, 1, 4);
      assertEquals(handler.textBufferToIso8601Date(), now);
   }

   private class BasePBResponseHandlerImpl extends BasePBResponseHandler<String> {
      public BasePBResponseHandlerImpl(DateCodecFactory dateCodecFactory) {
         super(dateCodecFactory);
      }

      @Override
      public String getResult() {
         return null;
      }
   }

}
