package org.jclouds.profitbricks.examples;

import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.*;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.profitbricks.compute.options.PBTemplateOptions;
import org.jclouds.profitbricks.domain.AvailabilityZone;
import org.jclouds.profitbricks.domain.OSType;
import org.jclouds.profitbricks.domain.specs.FirewallRuleCreationSpec;
import org.jclouds.profitbricks.domain.specs.ServerCreationSpec;

import java.util.Set;

/**
 * Illustrates ProfitBricks Server API usage.
 *
 * @author Serj Sintsov
 */
public class ServerCreationViaTemplateOptionsDemo extends BaseExample {

   public static void main(String[] args) throws RunNodesException {
      initComputeServiceContext();

      listNodes();

      createNode();

      listNodes();
   }

   private static ComputeMetadata createNode() throws RunNodesException {
      log(">> create server via custom template options");

      Template nodeCfg = cs.templateBuilder()
            .build();

      PBTemplateOptions pbOpts = nodeCfg.getOptions().as(PBTemplateOptions.class);
      pbOpts.serverSpec(ServerCreationSpec.builder()
            .serverName("Server_via_tpl_opts")
            .availabilityZone(AvailabilityZone.ZONE_1)
            .bootFromStorageId("ee857448-e645-46dc-a9a9-0979a56d0061")
            .cores(1)
            .ram(1024)
            .internetAccess(true)
            .osType(OSType.LINUX)
            .build()
      ).addFirewallRuleSpec(FirewallRuleCreationSpec.builder()
            .protocol(IpProtocol.TCP) // allow SSH connections from any source to any target IP
            .fromPort(22)
            .toPort(22)
            .build()
      );

      Set<? extends NodeMetadata> createdNodesSet = cs.createNodesInGroup("single_group", 1, nodeCfg);
      NodeMetadata createdNode = createdNodesSet.iterator().next();

      log(createdNode.toString());

      log("<< create server via custom template options");
      log("");
      return createdNode;
   }

   private static Set<? extends ComputeMetadata> listNodes() {
      log(">> listing all nodes");

      Set<? extends ComputeMetadata> nodesSet = cs.listNodes();
      if (nodesSet.isEmpty())
         log("   there are no any nodes");
      for (ComputeMetadata node : nodesSet) {
         log(node.toString());
      }

      log("<< listing all nodes");
      log("");
      return nodesSet;
   }

}
