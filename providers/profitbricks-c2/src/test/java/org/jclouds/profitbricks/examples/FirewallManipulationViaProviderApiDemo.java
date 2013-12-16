package org.jclouds.profitbricks.examples;

import org.jclouds.compute.RunNodesException;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.domain.Firewall;
import org.jclouds.profitbricks.domain.FirewallRule;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;

import java.util.Set;

/**
 * Illustrates ProfitBricks Firewall API usage.
 *
 * @author Serj Sintsov
 */
public class FirewallManipulationViaProviderApiDemo extends BaseExample {

   public static void main(String[] args) throws RunNodesException, InterruptedException {
      initComputeServiceContext();

      String nicId = "66437ecb-6e9a-4579-9da8-7b87cc40273b";

      listAllFirewalls();
      Firewall firewallByNIC = getFirewallByNIC(nicId);

      waitForFirewallAvailableState(firewallByNIC.getId());

      if (!firewallByNIC.isActive())
         activate(firewallByNIC.getId());
      else
         deactivate(firewallByNIC.getId());

      waitForFirewallAvailableState(firewallByNIC.getId());

      addRuleToNic(nicId, FirewallRuleCreationSpec.builder()
            .sourceIp("172.168.38.3")
            .protocol(IpProtocol.TCP)
            .fromPort(1)
            .toPort(400)
            .build()
      );

      waitForFirewallAvailableState(firewallByNIC.getId());

      firewallByNIC = getFirewallById(firewallByNIC.getId());
      for (FirewallRule rule : firewallByNIC.getRules())
         removeRuleById(rule.getId());

      waitForFirewallAvailableState(firewallByNIC.getId());

      deleteFirewall(firewallByNIC.getId());
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

   private static Firewall getFirewallByNIC(String nicId) {
      log(">> getting firewall for nic=" + nicId);

      Set<Firewall> firewalls = providerApi.firewallApi().listFirewalls();
      Firewall fwForNic = null;
      for (Firewall fw : firewalls) {
         if (fw.getNicId().equals(nicId)) {
            fwForNic = fw;
            break;
         }
      }
      if (fwForNic == null)
         log("   nic doesn't have firewall");
      else
         log(fwForNic.toString());

      log("<< getting firewall for nic=" + nicId);
      log("");
      return fwForNic;
   }

   private static Firewall getFirewallById(String firewallId) {
      log(">> getting firewall by id=" + firewallId);

      Firewall firewall = providerApi.firewallApi().getFirewall(firewallId);
      if (firewall == null)
         log("   nic doesn't have firewall");
      else
         log(firewall.toString());

      log("<< getting firewall by id=" + firewallId);
      log("");

      return firewall;
   }

   private static void activate(String firewallId) {
      log(">> activating firewall " + firewallId);
      providerApi.firewallApi().activateFirewall(firewallId);
      log("<< activating firewall " + firewallId);
      log("");
   }

   private static void deactivate(String firewallId) {
      log(">> deactivating firewall " + firewallId);
      providerApi.firewallApi().deactivateFirewall(firewallId);
      log("<< deactivating firewall " + firewallId);
      log("");
   }

   private static void addRuleToNic(String nicId, FirewallRuleCreationSpec ruleSpec) {
      log(">> adding firewall rule to nic " + nicId);
      providerApi.firewallApi().addFirewallRule(nicId, ruleSpec);
      log("<< adding firewall rule to nic " + nicId);
      log("");
   }

   private static void removeRuleById(String ruleId) {
      log(">> removing firewall rule by id=" + ruleId);
      providerApi.firewallApi().removeFirewallRule(ruleId);
      log("<< removing firewall rule by id=" + ruleId);
      log("");
   }

   private static void deleteFirewall(String firewallId) {
      log(">> delete firewall by id=" + firewallId);
      providerApi.firewallApi().deleteFirewall(firewallId);
      log("<< delete firewall by id=" + firewallId);
      log("");
   }

   private static void waitForFirewallAvailableState(String firewallId) throws InterruptedException {
      final int RETRY_NUM = 200;

      log("waiting until firewall provision state is AVAILABLE [" + firewallId + "]");


      for (int i = 0; i < RETRY_NUM; i++) {
         Firewall firewall = providerApi.firewallApi().getFirewall(firewallId);
         if (firewall.getProvisioningState() == ProvisioningState.AVAILABLE) {
            log("");
            return;
         }
         else
            Thread.sleep((long) 5*1000); // wait 5 sec
      }

      log("timeout exceed");
   }

}
