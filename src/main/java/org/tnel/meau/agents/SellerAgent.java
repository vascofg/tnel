package org.tnel.meau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import org.tnel.meau.participants.Seller;

import java.math.BigDecimal;
public class SellerAgent extends Agent {

    Seller seller;
    AID buyer;

    public SellerAgent(Seller seller) {
        this.seller = seller;
    }

    @Override
    protected void setup() {

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
        addBehaviour(new CyclicBehaviour(this) {

            String message;
            BigDecimal currentBestOffer;
            ACLMessage msg;

            @Override
            public void action() {
                msg = receive();

                if (msg != null) {
                    message = msg.getContent();
                    switch (msg.getPerformative()) {
                        // Mensagem com categoria de produtos a negociar
                        case ACLMessage.CFP:
                            System.out.println("CFP recebido: " + message);

                            if (!message.equals(seller.getProduct().getCategory())) {
                                return;
                            }

                            SellerAgent.this.buyer = msg.getSender();

                            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
                            propose.addReceiver(buyer);
                            int id = SellerAgent.this.seller.getProduct().getId();
                            propose.setContent(id + "");

                            System.out.println("[SELLER " + myAgent.getLocalName() + "] Enviada proposta");
                            send(propose);
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            System.out.println("accept proposal recebido por " + getName());
                            SellerAgent.this.buyer = msg.getSender();
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            System.out.println("reject proposal recebido por " + getName());
                            SellerAgent.this.buyer = msg.getSender();
                            //obter melhor proposta
                            currentBestOffer = new BigDecimal(message);

                            //ajustar proposta
                            System.out.println("[SELLER " + getLocalName() + "] BEST OFFER: " + currentBestOffer);
                            seller.getProduct().setPrice(currentBestOffer.subtract(seller.getDecrement()));

                            if (seller.getProduct().getPrice().compareTo(seller.getMinPrice()) == -1) {
                                ACLMessage leaveAuction = new ACLMessage(ACLMessage.INFORM);
                                leaveAuction.addReceiver(buyer);
                                leaveAuction.setContent("leave");
                                send(leaveAuction);
                                seller.getProduct().reset();
                                myAgent.doSuspend();
                                return;
                            }

                            break;
                        case ACLMessage.INFORM:
                            System.out.println("inform recebido");

                            if (msg.getContent().equals("deal"))
                                System.out.println("negocio feito com " + getName());
                            else if (msg.getContent().equals("over")) {
                                System.out.println("leilao acabou, nada para mim :( - " + getName());
                                seller.getProduct().reset();
                            }

                        default:
                            break;
                    }
                } else
                    block();
            }
        });
        this.doSuspend();
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
