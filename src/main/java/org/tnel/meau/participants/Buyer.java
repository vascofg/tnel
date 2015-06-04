package org.tnel.meau.participants;

import com.wordnik.swagger.annotations.ApiModel;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.BuyerAgent;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "buyer")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Buyer", description = "Buyer model")
public class Buyer {
    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "category", required = true)
    String category;

    //@XmlAttribute(name = "maxBuyPrice", required = true)
    //float maxBuyPrice;

    @XmlAttribute(name = "maxrounds", required = true)
    int maxrounds;

    @XmlAttribute(name = "timeout", required = true)
    int timeout;

    @XmlTransient
    Object doneNotifier;

    @XmlTransient
    Seller bestOfferSeller;

    @XmlTransient
    protected AgentController agentController;

    @XmlTransient
    private BigDecimal bestOffer;

    public Buyer() {
    }

    public Buyer(String name, String category, int maxrounds, Object doneNotifier, ContainerController containerController) {
        this.name = name;
        this.category = category;
        this.maxrounds = maxrounds;
        this.doneNotifier = doneNotifier;
        try {
            this.createAgent(containerController);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public Agent createAgent(ContainerController containerController) throws StaleProxyException {
        Agent agent = new BuyerAgent(this);
        this.agentController = containerController.acceptNewAgent(this.getName(), agent);
        this.agentController.start();
        return agent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMaxrounds() {
        return maxrounds;
    }

    public void setMaxrounds(int maxrounds) {
        this.maxrounds = maxrounds;
    }

    public Object getDoneNotifier() {
        return doneNotifier;
    }

    public void setDoneNotifier(Object doneNotifier) {
        this.doneNotifier = doneNotifier;
    }

    public Seller getBestOfferSeller() {
        return bestOfferSeller;
    }

    public void setBestOfferSeller(Seller bestOfferSeller) {
        this.bestOfferSeller = bestOfferSeller;
    }

    public AgentController getAgentController() {
        return agentController;
    }

    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public BigDecimal getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(BigDecimal bestOffer) {
        this.bestOffer = bestOffer;
    }

    @Override
    public String toString() {
        return "------------------\n" +
                "BUYER" +
                "Name: " + this.name + '\n' +
                "Category: " + this.category + '\n' +
                "Max rounds: " + this.maxrounds + '\n' +
                "Timeout: " + this.timeout + '\n' +
                "------------------";
    }

    public static boolean isValid(Buyer buyer) {
        return (buyer.getMaxrounds() > 0 && buyer.getTimeout() > 0 && buyer.getCategory() != null &&
                !buyer.getCategory().isEmpty() && buyer.getName() != null &&
                !buyer.getName().isEmpty());
    }
}
