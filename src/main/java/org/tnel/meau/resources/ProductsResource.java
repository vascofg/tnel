package org.tnel.meau.resources;

import org.tnel.meau.Meau;
import org.tnel.meau.items.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
public class ProductsResource {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Product> getProducts() {
        return Meau.products;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Product addProduct(Product product) {
        Meau.products.add(product);
        return product;
    }

}
