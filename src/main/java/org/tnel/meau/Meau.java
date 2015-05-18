package org.tnel.meau;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.items.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Meau {

    public static List<Product> products= new LinkedList<Product>();
    public static jade.wrapper.AgentContainer mainContainer = null;

    public static void main(String[] args) throws IOException, StaleProxyException {
        products.add(new Product(1, "Batatas vermelhas", "Batatas ideais para fritar", 20));
        products.add(new Product(2,"Batatas brancas","Batatas ideais para cozer", 18));
        
        final String baseUri = "http://localhost:"+(System.getenv("PORT")!=null?System.getenv("PORT"):"9998")+"/";
        final Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages","org.tnel.meau.resources");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format("Jersey started with WADL available at %sapplication.wadl.",baseUri, baseUri));

        // Get a hold on JADE runtime
        jade.core.Runtime rt = jade.core.Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);
        System.out.print("[JADE]runtime created\n");

        // Create a default profile
        Profile profile = new ProfileImpl(null, 1200, null);
        System.out.print("[JADE]profile created\n");

        System.out.println("[JADE]Launching a whole in-process platform..."+profile);
        mainContainer = rt.createMainContainer(profile);

        // now set the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        System.out.println("[JADE]Launching the agent container ..."+pContainer);

        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
        System.out.println("[JADE]Launching the agent container after ..."+pContainer);

        System.out.println("[JADE]containers created");
        /*System.out.println("[JADE]Launching the rma agent on the main container ...");
        AgentController rma = mainContainer.createNewAgent("rma",
                "jade.tools.rma.rma", new Object[0]);
        rma.start();*/

    }
}
