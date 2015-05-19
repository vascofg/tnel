package org.tnel.meau.items;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Attribute implements Comparable<Attribute> {

    @XmlAttribute(name = "name", required = true)
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract Object getValue();

    abstract void setValue(Object value);

    public Attribute() {
    }

    public Attribute(String name) {
        this.name = name;
    }

}
