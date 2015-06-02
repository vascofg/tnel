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

    @XmlAttribute(name = "category")
    String category;

    @XmlAttribute(name = "maxBuyPrice")
    float maxBuyPrice;

    public static final String agentClassName = BuyerAgent.class.getName();

    public BuyerAgent() {
    }

    public BuyerAgent(float maxBuyPrice, String category) {
        this.maxBuyPrice = maxBuyPrice;
        this.category = category;
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
        System.out.println("Enviado primeiro CFP");

        // Receber propostas de negocio
        addBehaviour(new SimpleBehaviour() {

            AID bestOfferAgent;
            ArrayList<AID> sellerAgents = new ArrayList<>();
            float bestOffer = -1;
            int numberOfSellers = 0, numberOfRounds = 0;

            ACLMessage msg;

            @Override
            public void action() {
                msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.PROPOSE:
                            System.out.println("recebida proposta " + msg.getContent());
                            sellerAgents.add(msg.getSender());
                            numberOfSellers++;
                            calculateValue(msg.getContent(), msg.getSender());
                            break;
                        case ACLMessage.INFORM:
                            sellers.remove(sellers.size()-1);
                            System.out.println("recebido inform para sair. N de vendedores atual: " + sellers.size());
                    }
                }
                else
                    block();
            }

            @Override
            public boolean done() {
                // Se ja tiver recebido propostas de todos os sellers, entao responde
                if (numberOfSellers == sellers.size() && msg != null) {
                    ACLMessage accept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    ACLMessage reject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);

                    accept.setContent("accepted");
                    accept.addReceiver(bestOfferAgent);
                    send(accept);

                    reject.setContent("rejected");
                    for (int i = 0; i < sellerAgents.size(); i++)
                        if (!sellerAgents.get(i).equals(bestOfferAgent))
                            reject.addReceiver(sellerAgents.get(i));

                    send(reject);
                    numberOfRounds++;

                    //Wait for sellers to adjust their proposals
                    try {
                        Thread.sleep(500);
                        System.out.println("---Waiting---");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Verificar fim de leilao
                    if (numberOfRounds == 5 || sellers.size() == 1){
                        ACLMessage bestOffer = new ACLMessage(ACLMessage.INFORM);
                        bestOffer.setContent("deal");
                        bestOffer.addReceiver(bestOfferAgent);
                        send(bestOffer);

                        ACLMessage otherOffers = new ACLMessage(ACLMessage.INFORM);
                        otherOffers.setContent("over");

                        for (int i = 0; i < sellerAgents.size(); i++)
                            if (!sellerAgents.get(i).equals(bestOfferAgent))
                                otherOffers.addReceiver(sellerAgents.get(i));
                        send(otherOffers);

                        return true;
                    }
                    else if (numberOfRounds < 5) {
                        // Envia novo CFP
                        ACLMessage message = new ACLMessage(ACLMessage.CFP);
                        message.setContent(category);

                        for (int i = 0; i < sellers.size(); i++)
                            message.addReceiver(sellerAgents.get(i));

                        System.out.println("Enviado CFP");
                        send(message);
                        sellerAgents = new ArrayList<>();
                        numberOfSellers = 0;
                    }
                }
                return false;
            }

            private void calculateValue(String messageContent, AID agentSender) {
                Product product = Meau.getProductById(Integer.parseInt(messageContent));
                float price = product.getPrice();
                System.out.println("Preco da proposta: " + price);

                //Se o preco da proposta for mais baixo que o melhor atual, atualiza
                if (price < bestOffer || bestOffer == -1) {
                    bestOffer = price;
                    bestOfferAgent = agentSender;
                }
            }
        });
    }
}
