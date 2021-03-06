package org.tnel.meau.participants;

import com.wordnik.swagger.annotations.ApiModel;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.SellerAgent;
import org.tnel.meau.items.Product;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Seller", description = "Seller model")
public class Seller {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlTransient
    protected AgentController agentController;

    protected Product product;
    String strategy;
    BigDecimal minPrice, decrement;

    public Seller() {
    }

    public Seller(String name, Product product, ContainerController containerController, String strategy, BigDecimal decrement, BigDecimal minPrice) {
        this.name = name;
        this.product = product;
        this.minPrice = minPrice;
        this.strategy = strategy;
        this.decrement = decrement;
        try {
            this.createAgent(containerController);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public Seller(Seller other) {
        this.name = other.name;
        this.product = new Product(other.product);
        this.minPrice = other.minPrice;
        this.strategy = other.getStrategy();
        this.decrement = other.getDecrement();
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

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setDecrement(BigDecimal decrement) {
        this.decrement = decrement;
    }

    public BigDecimal getDecrement() {
        return decrement;
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

    public static boolean isValid(Seller seller) {
        return (seller.getName() != null && !seller.getName().isEmpty() &&
                Product.isValid(seller.getProduct()) &&
                seller.decrement.compareTo(BigDecimal.ZERO) == 1 &&
                seller.minPrice.compareTo(BigDecimal.ZERO) == 1);
    }
}
