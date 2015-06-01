package org.tnel.meau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.tnel.meau.Meau;
import org.tnel.meau.items.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuyerAgent extends Agent {

    @XmlElementWrapper(name = "attributePreferences")
    @XmlElements({
            @XmlElement(name = "boolean", type = BooleanAttribute.class),
            @XmlElement(name = "descriptive", type = DescriptiveAttribute.class),
            @XmlElement(name = "numeric", type = NumericAttribute.class)})
    private HashMap<Attribute,Integer> attributePreferences;

    @XmlAttribute(name = "category")
    String category;

    @XmlAttribute(name = "maxBuyPrice")
    float maxBuyPrice;

    //float minProductUtility;

    public static final String agentClassName = BuyerAgent.class.getName();

    public BuyerAgent() {
    }

    public BuyerAgent(float maxBuyPrice, String category, HashMap<Attribute,Integer> attributePreferences) {
        this.maxBuyPrice = maxBuyPrice;
        this.category = category;
        this.attributePreferences = new HashMap<>(attributePreferences);
    }

    public HashMap<Attribute, Integer> getAttributePreferences() {
        return attributePreferences;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getMaxBuyPrice() {
        return maxBuyPrice;
    }

    public void setMaxBuyPrice(float maxBuyPrice) {
        this.maxBuyPrice = maxBuyPrice;
    }

    public void setAttributePreferences(HashMap<Attribute, Integer> attributePreferences) {
        this.attributePreferences = attributePreferences;
    }

    @Override
    protected void setup() {
        //super.setup();

        //registar agente na DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(BuyerAgent.class.getName());
        sd.setType("Buyer");
        System.out.println("Created Buyer Agent");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // Encontrar agentes do tipo seller
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Seller");
        template.addServices(sd1);

        ArrayList<DFAgentDescription> sellersDFAD = new ArrayList<>();
        try {
            sellersDFAD = new ArrayList<>(Arrays.asList(DFService.search(this, template)));
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        final ArrayList<DFAgentDescription> sellers = new ArrayList<>(sellersDFAD);


        //enviar mensagem para sellers com categoria de produto de interesse
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setContent(category);

        for (int i = 0; i < sellers.size(); i++)
            message.addReceiver(sellers.get(i).getName());

        send(message);

        final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

        // Receber propostas de negocio
        addBehaviour(new SimpleBehaviour() {

            AID bestOfferAgent;
            float bestOffer;

            private void calculateValue(String messageContent) {
                ArrayList<Attribute> matchingAttributes = new ArrayList<Attribute>();
                int productId = Integer.parseInt(messageContent);

                Product product = Meau.getProductById(productId);

                ArrayList<Attribute> attributes = new ArrayList<>(product.getAttributes());

                for (int i = 0; i < attributes.size(); i++) {
                    //if (attributePreferences.containsKey(attributes.get(i)))


                }


            }
            @Override
            public void action() {
                ACLMessage msg;
               // ArrayList<AID> sellerAgents = new ArrayList<>();

                for (int i = 0; i < sellers.size(); i++) {
                    msg = receive(mt);

                    /*if (!sellerAgents.contains(msg.getSender()))
                        sellerAgents.add(msg.getSender());*/

                    if (msg != null) {
                        System.out.println("recebida proposta " + msg.getContent());
                        calculateValue(msg.getContent());
                    }
                    else
                        block();
                }
            }

            @Override
            public boolean done() {
                return false;
            }
        });
            //}
        //});
    }
}
