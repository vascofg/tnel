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
        sd.setType(seller.getProduct().getCategory());
        System.out.println("[SELLER " + getLocalName() + "] READY TO ROLL!");
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
                            System.out.println("[SELLER " + getLocalName() + "] RECEIVED CFP");

                            SellerAgent.this.buyer = msg.getSender();

                            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
                            propose.addReceiver(buyer);
                            int id = SellerAgent.this.seller.getProduct().getId();
                            propose.setContent(id + "");

                            System.out.println("[SELLER " + getLocalName() + "] SENDING PROPOSE");
                            send(propose);
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            System.out.println("[SELLER " + getLocalName() + "] GOT ACCEPT_PROPOSAL");
                            SellerAgent.this.buyer = msg.getSender();
                            String[] content = message.split(" ");

                            currentBestOffer = seller.getProduct().getPrice();
                            Integer round = Integer.parseInt(content[0]);
                            Integer nRounds = Integer.parseInt(content[1]);

                            adjustProposal(true, round, round == nRounds);

                            System.out.println("[SELLER " + getLocalName() + "] NEW OFFER: " + seller.getProduct().getPrice() + " (previous offer accepted)");
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            System.out.println("[SELLER " + getLocalName() + "] GOT REJECT_PROPOSAL");
                            SellerAgent.this.buyer = msg.getSender();
                            content = message.split(" ");

                            currentBestOffer = new BigDecimal(content[0]);
                            round = Integer.parseInt(content[1]);
                            nRounds = Integer.parseInt(content[2]);

                            adjustProposal(false, round, round == nRounds);

                            System.out.println("[SELLER " + getLocalName() + "] NEW OFFER: " + seller.getProduct().getPrice() + " (previous offer rejected)");

                            break;
                        case ACLMessage.INFORM:
                            System.out.println("[SELLER " + getLocalName() + "] GOT INFORM");

                            if (msg.getContent().equals("deal"))
                                System.out.println("[SELLER " + getLocalName() + "] WON AUCTION");
                            else if (msg.getContent().equals("over")) {
                                System.out.println("[SELLER " + getLocalName() + "] LOST AUCTION");
                                seller.getProduct().reset();
                            }

                        default:
                            break;
                    }

                    if (seller.getProduct().getPrice().compareTo(seller.getMinPrice()) == -1) {
                        System.out.println("[SELLER " + getLocalName() + "] LEAVING AUCTION (RESERVE PRICE REACHED)");
                        ACLMessage leaveAuction = new ACLMessage(ACLMessage.INFORM);
                        leaveAuction.addReceiver(buyer);
                        leaveAuction.setContent("leave");
                        send(leaveAuction);
                        seller.getProduct().reset();
                        myAgent.doSuspend();
                        return;
                    }
                } else
                    block();
            }

            protected void adjustProposal(boolean bestOfferIsMine, int round, boolean lastRound) {

                BigDecimal decrement = new BigDecimal(0);

                switch (seller.getStrategy()) {
                    case "aggressive": // na primeira ronda decrementa logo um valor grande, nao fazendo mais nada o resto do leilao
                        if (round == 1)
                            seller.getProduct().setPrice(seller.getMinPrice().multiply(new BigDecimal(1.15)));

                        break;
                    case "reactive": //muda aposta se a dele for ultrapassada, nao tendo em conta a melhor oferta
                        if (!bestOfferIsMine)
                            decrement = seller.getDecrement();

                        System.out.println("--------------decremento "+ decrement);

                        seller.getProduct().setPrice(seller.getProduct().getPrice().subtract(decrement));
                        System.out.println("--------------preco fica a "+seller.getProduct().getPrice());
                        break;

                    case "progressive": //vai decrementando cada vez mais com o decorrer do leilao, nao tem em conta a melhor oferta
                        decrement = (seller.getDecrement().multiply(new BigDecimal(round))).divide(new BigDecimal(2)); // decremento * nRondas / 2
                        seller.getProduct().setPrice(seller.getProduct().getPrice().subtract(decrement));
                        break;

                    case "greedy": //decrementa todas as rondas independentemente se tem a melhor oferta e do preço da melhor oferta
                        seller.getProduct().setPrice(seller.getProduct().getPrice().subtract(decrement));
                        break;

                    case "lastround": //decrementa apenas no fim, a partir do preço da ultima melhor oferta
                        if (lastRound)
                            seller.getProduct().setPrice(currentBestOffer.subtract(decrement));
                        break;

                    case "smart": //decrementa quando a aposta dele por ultrapassada ou quando for o ultimo round
                        if (!bestOfferIsMine || lastRound)
                            seller.getProduct().setPrice(currentBestOffer.subtract(decrement));
                        break;
                    default:
                        break;

                }
            }
        });
        // comentar para testar so com java
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
