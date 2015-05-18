package org.example.resources;

import org.example.Main;
import org.example.items.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/products")
public class ProductsResource {

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<Product> getProducts() {
        return Main.products;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Product addProduct(Product product) {
        Main.products.add(product);
        return product;
    }

}
