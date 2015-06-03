package org.tnel.meau.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import jade.core.Agent;
import jade.wrapper.StaleProxyException;
import org.tnel.meau.Meau;
import org.tnel.meau.items.Product;
import org.tnel.meau.participants.Buyer;
import org.tnel.meau.participants.Seller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/participants")
@Api(value = "/participants", description = "Operations on participants")
public class ParticipantsResource {

    @GET
    @Path("/sellers")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all sellers")
    public List<Seller> getSellers() {
        return Meau.getSellers();
    }

    @Path("/sellers/{index}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get seller by index")
    public Seller getSeller(@PathParam("index") int index) {
        try {
            return Meau.getSellers().get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("[400] Element not found");
            throw new WebApplicationException(Response.status(
                    Response.Status.BAD_REQUEST).
                    entity("Element not found").
                    build());
        }
    }

    @POST
    @Path("/buyer")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a buyer and start the auction")
    public Product addBuyer(Buyer buyer) {
        try {
            if (Meau.auctionRunning) {
                System.out.println("[ERROR] Auction already running");
                throw new Exception();
            }

            Meau.auctionRunning = true;

            /* START SELLERS */
            System.out.println("STARTING SELLERS");
            int startedSellers = 0;
            for (int i = 0; i < Meau.getSellers().size(); i++) {
                Seller seller = Meau.getSellers().get(i);
                if (seller.getProduct().getCategory().equals(buyer.getCategory())) {
                    System.out.println("STARTING SELLER " + seller.getName());
                    seller.getAgentController().activate();
                    startedSellers++;
                }
            }

            if (startedSellers == 0) {
                System.out.println("[ERROR] No agents for that category");
                throw new Exception();
            }

            /* START BUYER */

            Object notifier = new Object();
            buyer.setDoneNotifier(notifier);
            System.out.println("STARTING BUYER");
            Agent buyerAgent = buyer.createAgent(Meau.mainContainer);
            buyerAgent.waitUntilStarted();

            synchronized (buyer.getDoneNotifier()) {
                //auction timeout
                notifier.wait(10000);
            }
        } catch (StaleProxyException e) {
            System.out.println("[ERROR] Error creating buyer agent");
            throw new Exception();
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Auction interrupted");
            throw new Exception();
        } finally {
            Meau.auctionRunning = false;

            if (buyer.getBestOfferSeller() == null) {
                System.out.println("[500] Auction unsuccessful");
                throw new WebApplicationException(Response.status(
                        Response.Status.INTERNAL_SERVER_ERROR).
                        entity("Auction unsuccessful").
                        build());
            }

            Seller wonSeller = buyer.getBestOfferSeller();
            //clone product
            Product wonProduct = new Product(wonSeller.getProduct());
            //reset product price
            wonSeller.getProduct().reset();
            //kill agent
            try {
                buyer.getAgentController().kill();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }

            return wonProduct;
        }
    }

    @POST
    @Path("/sellers")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert a seller")
    public Seller addSeller(Seller seller) {
        if (Meau.getSellers().add(seller)) {
            try {
                seller.createAgent(Meau.mainContainer);
                return seller;
            } catch (StaleProxyException e) {
                System.out.println("[500] Error creating agent");
                throw new WebApplicationException(Response.status(
                        Response.Status.INTERNAL_SERVER_ERROR).
                        entity("Error creating agent").
                        build());
            }
        } else
            System.out.println("[500] Error adding element");
        throw new WebApplicationException(Response.status(
                Response.Status.INTERNAL_SERVER_ERROR).
                entity("Error adding element").
                build());
    }


}
