package org.tnel.meau.participants;

import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.BuyerAgent;
import org.tnel.meau.agents.SellerAgent;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Seller extends Participant {

    @XmlAttribute(name = "sellerProperty")
    String sellerProperty = "seller test!";
    public static final String agentClassName = SellerAgent.class.getName();

    public Seller() {
    }

    public Seller(String name, ContainerController containerController) throws StaleProxyException {
        super(name, containerController, agentClassName);
    }

    public String getSellerProperty() {
        return sellerProperty;
    }

    public void setSellerProperty(String sellerProperty) {
        this.sellerProperty = sellerProperty;
    }
}
