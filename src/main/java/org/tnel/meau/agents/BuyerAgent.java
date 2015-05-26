package org.tnel.meau.agents;

import jade.core.Agent;
import org.tnel.meau.items.Attribute;
import org.tnel.meau.items.BooleanAttribute;
import org.tnel.meau.items.DescriptiveAttribute;
import org.tnel.meau.items.NumericAttribute;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import java.util.HashMap;

public class BuyerAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
    }

    @XmlElementWrapper(name = "attributePreferences")
    @XmlElements({
            @XmlElement(name = "boolean", type = BooleanAttribute.class),
            @XmlElement(name = "descriptive", type = DescriptiveAttribute.class),
            @XmlElement(name = "numeric", type = NumericAttribute.class)})
    private HashMap<Attribute,Integer> attributePreferences;

    @XmlAttribute(name = "category")
    String category;

    @XmlAttribute(name = "maxBuyPrice")
    float maxBuyPrice;

    //float minProductUtility;

    public static final String agentClassName = BuyerAgent.class.getName();

    public BuyerAgent() {
    }

    public void BuyerAgent(float maxBuyPrice, String category, HashMap<Attribute,Integer> attributePreferences) {
        this.maxBuyPrice = maxBuyPrice;
        this.category = category;
        this.attributePreferences = new HashMap<>(attributePreferences);
    }

    public HashMap<Attribute, Integer> getAttributePreferences() {
        return attributePreferences;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getMaxBuyPrice() {
        return maxBuyPrice;
    }

    public void setMaxBuyPrice(float maxBuyPrice) {
        this.maxBuyPrice = maxBuyPrice;
    }

    public void setAttributePreferences(HashMap<Attribute, Integer> attributePreferences) {
        this.attributePreferences = attributePreferences;
    }
}
