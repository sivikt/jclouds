package org.jclouds.profitbricks.domain;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Describes all ProfitBricks possible regions
 *
 * @author Serj Sintsov
 */
public enum Regions {
   NORTH_AMERICA, EUROPE, DEFAULT;

   public String value() {
      return name();
   }

   public static final Set<String> profitBricksC2Regions = ImmutableSet.of(NORTH_AMERICA.value(), EUROPE.value(),
         DEFAULT.value());

   public static Regions fromValue(String value) {
      try {
         return valueOf(value);
      } catch (IllegalArgumentException e) {
         return DEFAULT;
      }
   }
}
