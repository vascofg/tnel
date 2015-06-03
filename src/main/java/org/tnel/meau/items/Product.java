package org.tnel.meau.items;

import com.wordnik.swagger.annotations.ApiModel;

import javax.xml.bind.annotation.*;

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

    @XmlTransient
    protected Float initialPrice;

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
        this.initialPrice = price;
    }

    //clone constructor
    public Product(Product other) {
        this.name = other.name;
        this.description = other.description;
        this.price = other.price;
        this.category = other.category;
        this.id = other.id;
        this.initialPrice = other.price;
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
        if (initialPrice == null)
            this.initialPrice = price;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void reset() {
        this.price = initialPrice;
    }

    @Override
    public String toString() {
        return "------------------\n" +
                "PRODUCT" +
                "Id: " + this.id + '\n' +
                "Name: " + this.name + '\n' +
                "Description: " + this.description + '\n' +
                "Price: " + this.price + '\n' +
                "Category: " + this.category + '\n' +
                "------------------";
    }
}