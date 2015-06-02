package org.tnel.meau;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.tnel.meau.agents.BuyerAgent;
import org.tnel.meau.items.*;
import org.tnel.meau.participants.Seller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Meau {

    private static List<Product> products = new LinkedList<>();
    private static List<Seller> sellers = new LinkedList<>();
    public static jade.wrapper.AgentContainer mainContainer = null;


    public static List<Product> getProducts() {
        return products;
    }

    public static List<Seller> getSellers() {
        return sellers;
    }

    public static Product getProductById(int id) {
        for (int i = 0; i < getProducts().size(); i++)
            if (getProducts().get(i).getId() == id)
                return getProducts().get(i);

        return null;
    }

    public static void main(String[] args) throws Exception {
        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        final Server server = new Server(Integer.valueOf(webPort));
        final WebAppContext root = new WebAppContext();

        root.setContextPath("/");
        // Parent loader priority is a class loader setting that Jetty accepts.
        // By default Jetty will behave like most web containers in that it will
        // allow your application to replace non-server libraries that are part of the
        // container. Setting parent loader priority to true changes this behavior.
        // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);

        final String webappDirLocation = "src/main/webapp/";
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);

        server.setHandler(root);

        // Start JADE

        // Get a hold on JADE runtime
        jade.core.Runtime rt = jade.core.Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        // Create a default profile
        Profile profile = new ProfileImpl(null, 1200, null);

        mainContainer = rt.createMainContainer(profile);

        // now set the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);

       // jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);

        System.out.println("[JADE]Launching the rma agent on the main container ...");
        AgentController rma = mainContainer.createNewAgent("rma",
                "jade.tools.rma.rma", new Object[0]);
        rma.start();

        // Add sample data
        products.add(new Product("Batatas vermelhas", "Batatas ideais para fritar", 20, "batatas"));
        products.add(new Product("Batatas brancas", "Batatas ideais para cozer", 18, "batatas"));

        sellers.add(new Seller("REI DAS BATATAS", products.get(0), mainContainer, 2, 18));
        sellers.add(new Seller("VASCO BATATAS NO CU", products.get(1), mainContainer, 1, 7));

        mainContainer.acceptNewAgent("Buyerino", new BuyerAgent(50, "batatas")).start();

        // Start server
        server.start();
        System.out.println("[JETTY] Started server: " + server.getURI());
        server.join();
    }
}
