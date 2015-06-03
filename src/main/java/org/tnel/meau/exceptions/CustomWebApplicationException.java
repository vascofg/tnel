package org.tnel.meau.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Vasco on 03/06/2015.
 */
public class CustomWebApplicationException extends WebApplicationException {

    public CustomWebApplicationException(Response.Status status) {
        super(status);
    }

    public CustomWebApplicationException(Response.Status status, String message) {
        super(Response.status(status).
                entity(message).type("text/plain").build());
    }
}
