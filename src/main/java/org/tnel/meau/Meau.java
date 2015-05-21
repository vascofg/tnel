package org.tnel.meau;

import jade.core.Profile;
import jade.core.ProfileImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.tnel.meau.items.BooleanAttribute;
import org.tnel.meau.items.DescriptiveAttribute;
import org.tnel.meau.items.NumericAttribute;
import org.tnel.meau.items.Product;
import org.tnel.meau.participants.Buyer;
import org.tnel.meau.participants.Participant;
import org.tnel.meau.participants.Seller;

import java.util.LinkedList;
import java.util.List;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Meau {

    private static List<Product> products = new LinkedList<>();
    private static List<Participant> participants = new LinkedList<>();
    public static jade.wrapper.AgentContainer mainContainer = null;


    public static List<Product> getProducts() {
        return products;
    }


    public static List<Participant> getParticipants() {
        return participants;
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
        /*ProfileImpl pContainer = new ProfileImpl(null, 1200, null);

        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);

        System.out.println("[JADE]Launching the rma agent on the main container ...");
        AgentController rma = mainContainer.createNewAgent("rma",
                "jade.tools.rma.rma", new Object[0]);
        rma.start(); */

        // Add sample data
        products.add(new Product("Batatas vermelhas", "Batatas ideais para fritar", 20));
        products.add(new Product("Batatas brancas", "Batatas ideais para cozer", 18));

        products.get(0).addAttribute(new BooleanAttribute("descascadas", true));
        products.get(0).addAttribute(new NumericAttribute("peso", 1.5f));
        products.get(0).addAttribute(new DescriptiveAttribute("cor", "vermelho"));
        products.get(1).addAttribute(new NumericAttribute("peso", 2.25f));
        products.get(1).addAttribute(new DescriptiveAttribute("cor", "branco"));

        participants.add(new Buyer("Buyer 1", mainContainer));
        participants.add(new Seller("Seller 1", mainContainer));


        // Start server
        server.start();
        System.out.println("[JETTY] Started server: " + server.getURI());
        server.join();
    }
}
