package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.tnel.meau.Meau;
import org.tnel.meau.participants.Buyer;
import org.tnel.meau.participants.Participant;
import org.tnel.meau.participants.Seller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/participants")
@Api(value = "/participants", description = "Operations on participants")
public class ParticipantsResource {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all participants")
    public List<Participant> getParticipants() {
        return Meau.getParticipants();
    }

    @Path("/{index}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get participant by index")
    public Participant getParticipant(@PathParam("index") int index) {
        return Meau.getParticipants().get(index);
    }

    @POST
    @Path("/buyer")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a buyer")
    public Buyer addBuyer(Buyer buyer) {
        Meau.getParticipants().add(buyer);
        return buyer;
    }

    @POST
    @Path("/seller")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a seller")
    public Seller addSeller(Seller seller) {
        Meau.getParticipants().add(seller);
        return seller;
    }


}