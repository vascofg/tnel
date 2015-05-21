package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.tnel.meau.Meau;
import org.tnel.meau.items.BooleanAttribute;
import org.tnel.meau.items.DescriptiveAttribute;
import org.tnel.meau.items.NumericAttribute;
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
        return Meau.getProducts();
    }

    @Path("/{index}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get product by index")
    public Product getProduct(@PathParam("index") int index) {
        return Meau.getProducts().get(index);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a product")
    public Product addProduct(Product product) {
        Meau.getProducts().add(product);
        return product;
    }

    @Path("/{index}/attribute/boolean")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a boolean attribute to a product")
    public Product addBooleanAttribute(@PathParam("index") int index, BooleanAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        product.addAttribute(attribute);
        return product;
    }

    @Path("/{index}/attribute/descriptive")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a descriptive attribute to a product")
    public Product addDescriptiveAttribute(@PathParam("index") int index, DescriptiveAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        product.addAttribute(attribute);
        return product;
    }

    @Path("/{index}/attribute/numeric")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a numeric attribute to a product")
    public Product addNumericAttribute(@PathParam("index") int index, NumericAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        product.addAttribute(attribute);
        return product;
    }

}
