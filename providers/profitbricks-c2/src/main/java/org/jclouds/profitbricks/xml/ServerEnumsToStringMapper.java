package org.jclouds.profitbricks.xml;

import org.jclouds.profitbricks.domain.Server;

import javax.inject.Singleton;

/**
 * Maps {@link Server} specific enums to strings. Useful in the requests binders.
 *
 * @author Serj Sintsov
 */
@Singleton
public class ServerEnumsToStringMapper {

   public String mapOSType(Server.OSType osType) {
      return osType == null ? "" : osType.value();
   }

   public String mapAvailabilityZone(Server.AvailabilityZone zone) {
      return zone == null ? "" : zone.value();
   }

}
