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
    protected float price;
    @XmlElement(required = true)
    protected String category;

    protected int id;

    public Product() {
        this.id = NEXT_ID++;
    }

    public Product(String name, String description, float price, String category) {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }
}