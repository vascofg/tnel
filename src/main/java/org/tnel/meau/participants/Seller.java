package org.tnel.meau.participants;

import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.SellerAgent;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Seller extends Participant {

    @XmlAttribute(name = "sellerProperty")
    String sellerProperty = "seller test!";

    public Seller() {
    }

    public Seller(String name, ContainerController containerController) throws StaleProxyException {
        super(name, containerController, SellerAgent.class);
    }

    public String getSellerProperty() {
        return sellerProperty;
    }

    public void setSellerProperty(String sellerProperty) {
        this.sellerProperty = sellerProperty;
    }
}
