package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.tnel.meau.Meau;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/jade")
@Api(value = "/jade", description = "Interaction with JADE")
public class JadeResource {
    @Path("/platformState")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Get JADE main container state")
    public String getProducts() {
        return "JADE platform state: " + Meau.mainContainer.getState().toString();
    }
}
