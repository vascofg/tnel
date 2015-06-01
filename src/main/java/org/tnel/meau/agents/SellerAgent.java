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
import jade.wrapper.AgentController;
import org.tnel.meau.items.Product;
import org.tnel.meau.participants.Seller;

import java.io.IOException;

public class SellerAgent extends Agent {

    Seller seller;
    AID buyer;

    @Override
    protected void setup() {

        seller = (Seller) getArguments()[0];

        //registar agente na DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getName());
        sd.setType("Seller");
        System.out.println("Seller Agent " + getName() + " is ready to roll!");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

        // Receber mensagens
        addBehaviour(new SimpleBehaviour(this) {

            String message;
            ACLMessage msg;

            @Override
            public void action() {
                msg = receive();

                if (msg != null)
                    switch (msg.getPerformative()) {
                        // Mensagem com categoria de produtos a negociar
                        case ACLMessage.CFP:
                            message = msg.getContent();
                            System.out.println("msg recebida:" + message);
                            SellerAgent.this.buyer = msg.getSender();
                            break;

                    default:
                        break;
                }
                else
                    block();
            }

            @Override
            public boolean done() {
                if (msg != null) {
                    //se o produto em questao nao for da mesma categoria que o agente, entao este pode morrer
                    if(msg.getPerformative() == ACLMessage.CFP)
                       if (!message.equals(seller.getProduct().getCategory())) {
                           System.out.println("Agent " + getName() + "is dead.");
                           SellerAgent.this.doDelete();
                       }
                       //Se a categoria do produto em questao for igual, entao inicia-se a negociacao enviando a primeira proposta
                       else {
                           ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                           Product product = SellerAgent.this.seller.getProduct();

                           msg.addReceiver(buyer);
                           msg.setContent(product.getId()+"");
                           send(msg);
                       }
                }
                return false;
            }
        });

        //Receber resultados de propostas e fazer novas propostas
        addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {

            }

            @Override
            public boolean done() {
                return false;
            }
        });
    }

    public void SellerAgent() {}

}
