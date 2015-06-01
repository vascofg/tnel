package org.tnel.meau.participants;

import com.wordnik.swagger.annotations.ApiModel;
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

    public Seller() {
    }

    public Seller(String name, Product product, ContainerController containerController) {
        this.name = name;
        this.product = product;
        try {
            this.agentController = containerController.createNewAgent(this.name, SellerAgent.class.getName(), new Object[] {this});
            this.agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void createAgent(ContainerController containerController) throws StaleProxyException {
        this.agentController = containerController.createNewAgent(this.name, SellerAgent.class.getName(),
                new Object[]{});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
