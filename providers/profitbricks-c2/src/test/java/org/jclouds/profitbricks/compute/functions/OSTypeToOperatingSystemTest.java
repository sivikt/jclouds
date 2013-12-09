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
package org.jclouds.profitbricks.compute.functions;

import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.profitbricks.domain.OSType;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for {@link OSTypeToOperatingSystem}
 *
 * @author Serj Sintsov
 */
@Test(groups = "unit", testName = "OSTypeToOperatingSystemTest")
public class OSTypeToOperatingSystemTest {

   @Test
   public void testApply() {
      OSTypeToOperatingSystem func = new OSTypeToOperatingSystem();

      OperatingSystem res1 = func.apply(OSType.LINUX);
      assertNotNull(res1);
      assertEquals(res1.getFamily(), OsFamily.LINUX);
      assertEquals(res1.getDescription(), OsFamily.LINUX.value());

      OperatingSystem res2 = func.apply(OSType.WINDOWS);
      assertNotNull(res2);
      assertEquals(res2.getFamily(), OsFamily.WINDOWS);
      assertEquals(res2.getDescription(), OsFamily.WINDOWS.value());

      OperatingSystem res3 = func.apply(OSType.OTHER);
      assertNotNull(res3);
      assertEquals(res3.getFamily(), OsFamily.UNRECOGNIZED);
      assertEquals(res3.getDescription(), OsFamily.UNRECOGNIZED.value());

      OperatingSystem res4 = func.apply(OSType.UNKNOWN);
      assertNotNull(res4);
      assertEquals(res4.getFamily(), OsFamily.UNRECOGNIZED);
      assertEquals(res4.getDescription(), OsFamily.UNRECOGNIZED.value());
   }

}
