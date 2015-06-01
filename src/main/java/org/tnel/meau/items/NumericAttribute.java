package org.tnel.meau.items;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "numeric")
public class NumericAttribute extends Attribute {

    @XmlAttribute(name = "value", required = true)
    protected Float value;

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Float) value;
    }

    @Override
    String getType() {
        return "numeric";
    }

    @Override
    public int compareTo(Attribute other) {
        return this.value.compareTo((Float) other.getValue());
    }

    public NumericAttribute() {
    }

    public NumericAttribute(String name, Float value) {
        super(name);
        this.value = value;
    }
}
