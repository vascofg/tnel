package org.tnel.meau.resources;

import org.tnel.meau.Meau;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/jade")
public class JadeResource {
    @Path("/platformState")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getProducts() {
        return "JADE platform state: " + Meau.mainContainer.getState().toString();
    }
}
