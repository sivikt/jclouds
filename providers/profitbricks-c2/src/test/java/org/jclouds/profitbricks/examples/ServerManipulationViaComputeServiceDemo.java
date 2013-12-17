package org.jclouds.profitbricks.examples;

import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.*;

import java.util.Set;

/**
 * Illustrates ProfitBricks Server API usage.
 *
 * @author Serj Sintsov
 */
public class ServerManipulationViaComputeServiceDemo extends BaseExample {

   public static void main(String[] args) throws RunNodesException {
      initComputeServiceContext(args);

      String nodeId = "4b7e644d-b13d-45ae-9346-cfbc986c8785";
      NodeMetadata node = getNode(nodeId);

      if (node.getStatus() == NodeMetadata.Status.RUNNING)
         stopNode(node.getId());
      else
         log(">>> server is not in RUNNING status");

      node = getNode(nodeId);
      if (node.getStatus() == NodeMetadata.Status.SUSPENDED)
         startNode(node.getId());
      else
         log(">>> server is not in SUSPENDED status");

      rebootNode(node.getId());

      destroyNode(node.getId());

      listNodes();
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

   private static NodeMetadata getNode(String nodeId) {
      log(">> get server by id=" + nodeId);

      NodeMetadata nodeMetadata = cs.getNodeMetadata(nodeId);
      log(nodeMetadata.toString());

      log("<< get server by id=" + nodeId);
      log("");
      return nodeMetadata;
   }

   private static void stopNode(String nodeId) {
      log(">> stopping server by id=" + nodeId);

      cs.suspendNode(nodeId);
      log("   server has been suspended");

      log("<< stopping server by id=" + nodeId);
      log("");
   }

   private static void startNode(String nodeId) {
      log(">> starting server by id=" + nodeId);

      cs.resumeNode(nodeId);
      log("   server has been starting");

      log("<< starting server by id=" + nodeId);
      log("");
   }

   private static void rebootNode(String nodeId) {
      log(">> rebooting server by id=" + nodeId);

      cs.rebootNode(nodeId);
      log("   server has been rebooted");

      log("<< rebooting server by id=" + nodeId);
      log("");
   }

   private static void destroyNode(String nodeId) {
      log(">> deleting server by id=" + nodeId);

      cs.destroyNode(nodeId);
      log("   server has been deleted");

      log("<< deleting server by id=" + nodeId);
      log("");
   }

}
