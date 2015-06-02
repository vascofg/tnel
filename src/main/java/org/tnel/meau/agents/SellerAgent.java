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
                            System.out.println("CFP recebido: " + message);

                            //se o produto em questao nao for da mesma categoria que o agente, entao este pode morrer
                            if (!message.equals(seller.getProduct().getCategory())) {
                                System.out.println("Agent " + getName() + "has been properly terminated.");
                                SellerAgent.this.doDelete();
                            }

                            SellerAgent.this.buyer = msg.getSender();

                            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
                            propose.addReceiver(buyer);
                            int id = SellerAgent.this.seller.getProduct().getId();
                            propose.setContent(id+"");

                            System.out.println("Enviada proposta");
                            send(propose);
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            System.out.println("accept proposal recebido por " + getName());
                            SellerAgent.this.buyer = msg.getSender();
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            System.out.println("reject proposal recebido por " + getName());
                            SellerAgent.this.buyer = msg.getSender();
                            //ajustar proposta
                            seller.getProduct().setPrice(seller.getProduct().getPrice()-seller.getDecrement());
                            break;
                        case ACLMessage.INFORM:
                            System.out.println("inform recebido");

                            if (msg.getContent().equals("deal"))
                                System.out.println("negocio feito com " + getName());
                            else if (msg.getContent().equals("over"))
                                System.out.println("leilao acabou, nada para mim :( - " + getName());
                    default:
                        break;
                }
                else
                    block();
            }

            @Override
            public boolean done() {
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.INFORM)
                        return true;
                    else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL)
                        if (seller.getProduct().getPrice() < seller.getMinPrice()) {
                            ACLMessage leaveAuction = new ACLMessage(ACLMessage.INFORM);
                            leaveAuction.addReceiver(buyer);
                            leaveAuction.setContent("leave");
                            send(leaveAuction);

                            return true;
                        }
                }
                return false;
            }
        });
    }

    public void SellerAgent() {}

}
