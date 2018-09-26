package es.unex.giiis.pi.rednotes.resources.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class CustomBadRequestException extends WebApplicationException {

	private static final long serialVersionUID = -402752772426499120L;

	public CustomBadRequestException() {
		super(Response
				  .status(Response.Status.BAD_REQUEST)
				  .build());
	}

	public CustomBadRequestException(String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"status\" : \"404\", \"userMessage\" : \""+message+"\"}")
				.type("application/json")
				.build());
	}
}
