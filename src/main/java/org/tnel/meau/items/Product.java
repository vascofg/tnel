package org.tnel.meau.items;

import com.wordnik.swagger.annotations.ApiModel;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "product")
@ApiModel(value = "Product", description = "Product model")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected int price;

    @XmlElementWrapper(name = "attributes")
    @XmlElements({
            @XmlElement(name = "boolean", type = BooleanAttribute.class),
            @XmlElement(name = "descriptive", type = DescriptiveAttribute.class),
            @XmlElement(name = "numeric", type = NumericAttribute.class)})
    protected List<Attribute> attributeList = new LinkedList<>();

    public Product() {
    }

    public Product(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
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

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void addAttribute(Attribute attribute) {
        this.attributeList.add(attribute);
    }

}