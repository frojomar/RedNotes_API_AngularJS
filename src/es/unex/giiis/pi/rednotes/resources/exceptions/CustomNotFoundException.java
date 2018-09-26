package es.unex.giiis.pi.rednotes.resources.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CustomNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = -402752772426499120L;

	public CustomNotFoundException() {
		super(Response
			  .status(Response.Status.NOT_FOUND)
			  .build());
	}
	
	public CustomNotFoundException(String message) {
		super(Response
				.status(Response.Status.NOT_FOUND)
				.entity("{\"status\" : \"404\", \"userMessage\" : \""+message+"\"}")
				.type("application/json")
				.build());
	}
}
