package org.tnel.meau.participants;

import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.agents.BuyerAgent;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Buyer extends Participant {

    @XmlAttribute
    String buyerProperty = "buyer test!";

    public Buyer() {
    }

    public Buyer(String name, ContainerController containerController) throws StaleProxyException {
        super(name, containerController, BuyerAgent.class);
    }

    public String getBuyerProperty() {
        return buyerProperty;
    }

    public void setBuyerProperty(String buyerProperty) {
        this.buyerProperty = buyerProperty;
    }

    /*List<Attribute> attributePreferences;
    List<Integer> productValue;
    float maxBuyPrice, minProductUtility;
    Product category;

    Buyer(float maxBuyPrice, Product category) {
        this.maxBuyPrice = maxBuyPrice;
        this.category = category;
        attributePreferences = new LinkedList<>();
        setAttributePreferences(category.getAttributes());
    }

    void setAttributePreferences(List<Attribute> productAtrributes) {
        System.out.println("Product attributes:" + productAtrributes);

        Scanner in = new Scanner(System.in);

        for (int i = 0; i < productAtrributes.size(); i++) {
            System.out.println("Product attribute 1: " + productAtrributes.get(i).getName());
            System.out.print("\tDesired value: ");
            attributePreferences.add(new Attribute(productAtrributes.get(i).getName(), in.nextLine()));
        }
    }

    void printAttributePreferences() {
        System.out.println("Buyer preferences: ");
        for (int i = 0; i < attributePreferences.size(); i++) {
            System.out.println("Attribute: " + attributePreferences.get(i).getName());
            System.out.println("\tValue: " + attributePreferences.get(i).getValue());
        }
    }*/
}
