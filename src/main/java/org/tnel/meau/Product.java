import java.util.ArrayList;

public class Product {
    ArrayList<Attribute> attributes;

    Product(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
