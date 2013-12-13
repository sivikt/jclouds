package org.jclouds.profitbricks.examples;

import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.*;

import java.util.Date;
import java.util.Set;

/**
 * Illustrates ProfitBricks provider usage.
 *
 * @author Serj Sintsov
 */
public class ServerCreationViaComputeServiceDemo extends BaseExample {

   public static void main(String[] args) throws RunNodesException {
      initComputeServiceContext();

      listNodes();

      createNode();

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

   private static ComputeMetadata createNode() throws RunNodesException {
      log(">> create server");

      Hardware hardware = new HardwareBuilder()
            .id("")
            .name("Server_" + new Date())
            .ram(1024)
            .processor(new Processor(2, 0))
            .build();

      // ee115c89-c758-11e1-b18b-0025901dfe2a - WINDOWS
      OperatingSystem os = new OperatingSystem.Builder()
            .family(OsFamily.LINUX)
            .description(OsFamily.LINUX.value())
            .build();
      Image image = new ImageBuilder()
            .id("db2b3ab9-b0ec-11e2-a401-0025901dfe2a") // LINUX
            .operatingSystem(os)
            .status(Image.Status.AVAILABLE)
            .build();

      Template nodeCfg = cs.templateBuilder()
            .fromImage(image)
            .fromHardware(hardware)
            .build();

      Set<? extends NodeMetadata> createdNodesSet = cs.createNodesInGroup("singlegroup", 1, nodeCfg);
      NodeMetadata createdNode = createdNodesSet.iterator().next();

      log(createdNode.toString());

      log("<< create server");
      log("");
      return createdNode;
   }

}
