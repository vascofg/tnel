import java.util.ArrayList;
import java.util.Scanner;

public class Buyer {

    ArrayList<Attribute> attributePreferences;
    ArrayList<Integer> productValue;
    float maxBuyPrice, minProductUtility;
    Product category;

    Buyer(float maxBuyPrice, Product category) {
        this.maxBuyPrice = maxBuyPrice;
        this.category = category;
        attributePreferences = new ArrayList<Attribute>();
        setAttributePreferences(category.getAttributes());
    }

    void setAttributePreferences(ArrayList<Attribute> productAtrributes) {
        System.out.println("Product attributes:"+productAtrributes);

        Scanner in = new Scanner(System.in);

        for (int i = 0; i < productAtrributes.size(); i++) {
            System.out.println("Product attribute 1: " + productAtrributes.get(i).getName());
            System.out.print("\tDesired value: ");
            attributePreferences.add(new Attribute(productAtrributes.get(i).getName(), in.nextLine()));
        }
    }

    void printAttributePreferences() {
        System.out.println("Buyer preferences: ");
        for (int i = 0; i < attributePreferences.size(); i++) {
            System.out.println("Attribute: "+ attributePreferences.get(i).getName());
            System.out.println("\tValue: " + attributePreferences.get(i).getValue());
        }
    }
}
