package org.tnel.meau.items;

import com.wordnik.swagger.annotations.ApiModel;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "product")
@ApiModel(value = "Product", description = "Product model")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    private static int NEXT_ID = 1;

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected int price;
    @XmlElement(required = true)
    protected String category;

    protected int id;

    @XmlElementWrapper(name = "attributes")
    @XmlElements({
            @XmlElement(name = "boolean", type = BooleanAttribute.class),
            @XmlElement(name = "descriptive", type = DescriptiveAttribute.class),
            @XmlElement(name = "numeric", type = NumericAttribute.class)})
    protected List<Attribute> attributes = new LinkedList<>();

    public Product() {
        this.id = NEXT_ID++;
    }

    public Product(String name, String description, int price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.id = NEXT_ID++;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public boolean addAttribute(Attribute attribute) {
        return this.attributes.add(attribute);
    }

    public String getProductInfo() {
        String info = price+"";

        for (int i = 0; i < attributes.size(); i++)
            info += " " + attributes.get(i).getType() + " " + attributes.get(i).getName() + " " + attributes.get(i).getValue();

        return info;
    }

    public int getId() {
        return id;
    }
}