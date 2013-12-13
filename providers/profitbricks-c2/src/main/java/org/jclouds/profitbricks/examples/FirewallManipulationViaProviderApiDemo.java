package org.jclouds.profitbricks.examples;

import org.jclouds.compute.RunNodesException;
import org.jclouds.profitbricks.domain.Firewall;

import java.util.Set;

/**
 * Illustrates ProfitBricks provider usage.
 *
 * @author Serj Sintsov
 */
public class FirewallManipulationViaProviderApiDemo extends BaseExample {

   public static void main(String[] args) throws RunNodesException {
      initComputeServiceContext();

      listAllFirewalls();
   }

   private static Set<Firewall> listAllFirewalls() {
      log(">> listing all firewalls");

      Set<Firewall> firewalls = providerApi.firewallApi().listFirewalls();
      if (firewalls.isEmpty())
         log("   there are no any firewalls");
      for (Firewall fw : firewalls) {
         log(fw.toString());
      }

      log("<< listing all firewalls");
      log("");
      return firewalls;
   }

}
