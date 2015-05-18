package org.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import org.example.items.Product;

public class Main {

    public static List<Product> products= new LinkedList<Product>();

    public static void main(String[] args) throws IOException {
        products.add(new Product(1,"Batatas vermelhas","Batatas ideais para fritar", 20));
        products.add(new Product(2,"Batatas brancas","Batatas ideais para cozer", 18));
        
        final String baseUri = "http://localhost:"+(System.getenv("PORT")!=null?System.getenv("PORT"):"9998")+"/";
        final Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages","org.example.resources");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format("Jersey started with WADL available at %sapplication.wadl.",baseUri, baseUri));
    }
}
