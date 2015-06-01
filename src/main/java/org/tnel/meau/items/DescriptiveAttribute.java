package org.tnel.meau.items;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "descriptive")
public class DescriptiveAttribute extends Attribute {

    @XmlAttribute(name = "value", required = true)
    protected String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    String getType() {
        return "descriptive";
    }

    @Override
    public int compareTo(Attribute other) {
        return this.value.compareTo((String) other.getValue());
    }

    public DescriptiveAttribute() {
    }

    public DescriptiveAttribute(String name, String value) {
        super(name);
        this.value = value;
    }
}
