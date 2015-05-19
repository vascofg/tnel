import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        Attribute color = new Attribute("color", "red");
        Attribute size = new Attribute("size", "large");

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(color);
        attributes.add(size);

        Product p = new Product(attributes);
        Buyer b = new Buyer(100, p);
        b.printAttributePreferences();
    }
}
