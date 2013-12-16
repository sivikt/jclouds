package org.jclouds.profitbricks.examples;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.profitbricks.PBApi;

/**
 * Illustrates ProfitBricks provider usage.
 *
 * @author Serj Sintsov
 */
public class BaseExample {

   public static ComputeService cs;
   public static PBApi providerApi;

   public static void initComputeServiceContext() {
      ComputeServiceContext context = ContextBuilder.newBuilder("profitbricks-c2")
            .credentials("sergey.sintsov@altoros.com", "CN_JK@J5p3Q2wZ6bKe/b'DZ9J_i+0F(C3Q+o]*GAm]##([QK:O`P^,Cp\"(Kl/Fs")
            .buildView(ComputeServiceContext.class);

      cs = context.getComputeService();
      providerApi = context.unwrapApi(PBApi.class);
   }

   public static void log(String msg, Object... args) {
      System.out.println(String.format(msg, args));
   }

}
