package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.tnel.meau.Meau;
import org.tnel.meau.items.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
/*
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
        try {
            return Meau.getProducts().get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new WebApplicationException(Response.status(
                    Response.Status.BAD_REQUEST).
                    entity("Element not found").
                    build());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a product")
    public Product addProduct(Product product) {
        if (Meau.getProducts().add(product))
            return product;
        else
            throw new WebApplicationException(Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error adding element").
                    build());
    }

    @Path("/{index}/attribute/boolean")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a boolean attribute to a product")
    public Product addBooleanAttribute(@PathParam("index") int index, BooleanAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        if (product.addAttribute(attribute))
            return product;
        else
            throw new WebApplicationException(Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error adding element").
                    build());
    }

    @Path("/{index}/attribute/descriptive")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a descriptive attribute to a product")
    public Product addDescriptiveAttribute(@PathParam("index") int index, DescriptiveAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        if (product.addAttribute(attribute))
            return product;
        else
            throw new WebApplicationException(Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error adding element").
                    build());
    }

    @Path("/{index}/attribute/numeric")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a numeric attribute to a product")
    public Product addNumericAttribute(@PathParam("index") int index, NumericAttribute attribute) {
        Product product = Meau.getProducts().get(index);
        if (product.addAttribute(attribute))
            return product;
        else
            throw new WebApplicationException(Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error adding element").
                    build());
    }

}*/
