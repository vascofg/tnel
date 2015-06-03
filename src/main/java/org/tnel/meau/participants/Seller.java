package org.tnel.meau.participants;

import com.wordnik.swagger.annotations.ApiModel;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.SellerAgent;
import org.tnel.meau.items.Product;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Seller", description = "Seller model")
public class Seller {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlTransient
    protected AgentController agentController;

    protected Product product;

    float decrement, minPrice;

    public Seller() {
    }

    public Seller(String name, Product product, ContainerController containerController, float decrement, float minPrice) {
        this.name = name;
        this.product = product;
        this.decrement = decrement;
        this.minPrice = minPrice;
        try {
            this.createAgent(containerController);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public Agent createAgent(ContainerController containerController) throws StaleProxyException {
        Agent agent = new SellerAgent(this);
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

    public AgentController getAgentController() {
        return agentController;
    }

    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getDecrement() {
        return decrement;
    }

    public void setDecrement(float decrement) {
        this.decrement = decrement;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public String toString() {//String name, Product productfloat decrement, float minPrice
        return "------------------\n" +
                "SELLER" +
                "Name: " + this.name + '\n' +
                "Product: " + this.product + '\n' +
                "Decrement: " + this.decrement + '\n' +
                "Minimum price: " + this.minPrice + '\n' +
                "------------------";
    }
}
