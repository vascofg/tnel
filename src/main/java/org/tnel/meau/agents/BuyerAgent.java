package org.tnel.meau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.tnel.meau.Meau;
import org.tnel.meau.items.Product;
import org.tnel.meau.participants.Buyer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class BuyerAgent extends Agent {

    private Buyer buyer;

    public BuyerAgent(Buyer buyer) {
        this.buyer = buyer;
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

        // Receber propostas de negocio
        addBehaviour(new SimpleBehaviour() {

            AID bestOfferAgent;
            BigDecimal bestOffer = new BigDecimal(-1);
            ArrayList<AID> sellerAgents = new ArrayList<>();
            int numberOfSellers = 0, numberOfRounds = 0;

            ACLMessage msg;

            @Override
            public void onStart() {
                //enviar mensagem para sellers com categoria de produto de interesse
                ACLMessage message = new ACLMessage(ACLMessage.CFP);
                message.setContent(buyer.getCategory());

                for (int i = 0; i < sellers.size(); i++) {
                    message.addReceiver(sellers.get(i).getName());
                    System.out.println("GOING TO SEND CFP TO " + sellers.get(i).getName());
                }

                send(message);
                System.out.println("Enviado primeiro CFP");
                super.onStart();
            }

            @Override
            public void action() {
                msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.PROPOSE:
                            System.out.println("recebida proposta " + msg.getContent());
                            if(!sellerAgents.contains(msg.getSender())) {
                                sellerAgents.add(msg.getSender());
                                numberOfSellers++;
                                calculateValue(msg.getContent(), msg.getSender());
                            }
                            break;
                        case ACLMessage.INFORM:
                            sellers.remove(sellers.size() - 1);
                            System.out.println("recebido inform para sair. N de vendedores atual: " + sellers.size());
                    }
                } else
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

                    reject.setContent(bestOffer.toPlainString());
                    for (int i = 0; i < sellerAgents.size(); i++)
                        if (!sellerAgents.get(i).equals(bestOfferAgent))
                            reject.addReceiver(sellerAgents.get(i));

                    send(reject);
                    numberOfRounds++;

                    //Verificar fim de leilao
                    if (numberOfRounds == buyer.getMaxrounds() || sellers.size() == 1) {
                        ACLMessage bestOfferMsg = new ACLMessage(ACLMessage.INFORM);
                        bestOfferMsg.setContent("deal");
                        bestOfferMsg.addReceiver(bestOfferAgent);
                        send(bestOfferMsg);

                        ACLMessage otherOffers = new ACLMessage(ACLMessage.INFORM);
                        otherOffers.setContent("over");

                        for (int i = 0; i < sellerAgents.size(); i++)
                            if (!sellerAgents.get(i).equals(bestOfferAgent))
                                otherOffers.addReceiver(sellerAgents.get(i));
                        send(otherOffers);
                        System.out.println("WINNING AGENT NAME: " + bestOfferAgent.getLocalName());
                        buyer.setBestOffer(bestOffer);
                        buyer.setBestOfferSeller(Meau.getSellerByAgentName(bestOfferAgent.getLocalName()));
                        synchronized (buyer.getDoneNotifier()) {
                            buyer.getDoneNotifier().notify();
                        }
                        return true;
                    } else if (numberOfRounds < buyer.getMaxrounds()) {
                        // Envia novo CFP
                        ACLMessage message = new ACLMessage(ACLMessage.CFP);
                        message.setContent(buyer.getCategory());

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
                BigDecimal price = product.getPrice();
                System.out.println("Preco da proposta: " + price);

                //Se o preco da proposta for mais baixo que o melhor atual, atualiza
                if (price.compareTo(bestOffer) == -1 || bestOffer.compareTo(new BigDecimal("-1")) == 0) {
                    bestOffer = price;
                    bestOfferAgent = agentSender;
                }
            }
        });
        System.out.println("Created Buyer Agent");
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        super.takeDown();
    }
}
