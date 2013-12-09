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

import com.google.common.base.Function;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.profitbricks.domain.OSType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A function for transforming a ProfitBricks {@link OSType} to a native
 * {@link OperatingSystem} object.
 *
 * @author Serj Sintsov
 */
public class OSTypeToOperatingSystem implements Function<OSType, OperatingSystem> {

   @Override
   public OperatingSystem apply(OSType osType) {
      checkNotNull(osType, "osType");

      switch (osType) {
         case WINDOWS:
            return OperatingSystem.builder()
                  .description(OsFamily.WINDOWS.value())
                  .family(OsFamily.WINDOWS)
                  .build();
         case LINUX:
            return OperatingSystem.builder()
                  .description(OsFamily.LINUX.value())
                  .family(OsFamily.LINUX)
                  .build();
         default:
            return OperatingSystem.builder()
                  .description(OsFamily.UNRECOGNIZED.value())
                  .family(OsFamily.UNRECOGNIZED)
                  .build();
      }
   }

}
