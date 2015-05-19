package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.tnel.meau.Meau;
import org.tnel.meau.items.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Api(value = "/products", description = "Operations on products")
public class ProductsResource {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all products")
    public List<Product> getProducts() {
        return Meau.products;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a product")
    public Product addProduct(Product product) {
        Meau.products.add(product);
        return product;
    }

}
