package org.tnel.meau;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.tnel.meau.agents.BuyerAgent;
import org.tnel.meau.items.Product;
import org.tnel.meau.participants.Buyer;
import org.tnel.meau.participants.Seller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Meau {

    public static boolean auctionRunning = false;
    private static List<Product> products = new ArrayList<>();
    private static List<Seller> sellers = new ArrayList<>();
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

    public static Seller getSellerByAgentName(String name) {
        for (int i = 0; i < getSellers().size(); i++)
            if (getSellers().get(i).getName().equals(name))
                return getSellers().get(i);

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
        products.add(new Product("Batatas vermelhas", "Batatas ideais para fritar", new BigDecimal("20"), "batatas"));
        products.add(new Product("Batatas brancas", "Batatas ideais para cozer", new BigDecimal("20"), "batatas")); //18
        products.add(new Product("Batatas roxas", "Batatas ideais para assar", new BigDecimal("20"), "batatas")); //25
        products.add(new Product("Batatas amarelas", "Batatas ideais para puré", new BigDecimal("20"), "batatas")); //21
        products.add(new Product("Batatas pretas", "Batatas ideais para arroz", new BigDecimal("20"), "batatas")); //21

        //products.add(new Product("Portátil normal", "Standard", new BigDecimal("750"), "computadores"));

        sellers.add(new Seller("Rei das Batatas", products.get(0), mainContainer, "reactive", new BigDecimal("0.5"), new BigDecimal("16")));
        sellers.add(new Seller("El Bataton", products.get(1), mainContainer, "progressive", new BigDecimal("0.1"), new BigDecimal("14")));
        sellers.add(new Seller("Batatinha", products.get(2), mainContainer, "lastround", new BigDecimal("0.3"), new BigDecimal("12")));
        sellers.add(new Seller("Sr. Batata", products.get(3), mainContainer, "greedy", new BigDecimal("0.1"), new BigDecimal("15")));
        sellers.add(new Seller("Batatas Plus", products.get(4), mainContainer, "aggressive", new BigDecimal("0.1"), new BigDecimal("15")));
        //sellers.add(new Seller("Computex", products.get(4), mainContainer, "agressive", new BigDecimal("25"), new BigDecimal("500")));


        //new Buyer("Buyerino", "batatas", 5, new Object(), mainContainer);

        // Start server
        server.start();
        System.out.println("[JETTY] Started server: " + server.getURI());
        server.join();
    }
}
