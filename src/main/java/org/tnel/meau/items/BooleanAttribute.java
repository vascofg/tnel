package org.tnel.meau.items;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "boolean")
public class BooleanAttribute extends Attribute {

    @XmlAttribute(name = "value", required = true)
    protected Boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Boolean) value;
    }

    @Override
    public int compareTo(Attribute other) {
        return this.value.compareTo((Boolean) other.getValue());
    }

    public BooleanAttribute() {
    }

    public BooleanAttribute(String name, Boolean value) {
        super(name);
        this.value = value;
    }
}
