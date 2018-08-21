package rest;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import service.api.HealthCheckService;

@Component(value = "HealthCheck")
@Path("healthcheck")
public class Healthcheck {

	@Autowired
	@Qualifier("HealthCheckService")
	private HealthCheckService hcService;
	
	@Autowired
	@Qualifier("logger")
    private org.slf4j.Logger logger;
    
    public Healthcheck() {
    }

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getStatus() {

		try {
			String status = hcService.getStatus();
			logger.info("status = " + status);

			if (status.equals("ALIVE")) {
				return Response.status(Response.Status.OK).entity("Alive")
						.build();
			} else if (status.equals("BUSY")) {
				return Response.status(Response.Status.SERVICE_UNAVAILABLE)
						.entity("Busy").build();
			}

			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Internal server error due to unhandled healthcheck status = "
							+ status).build();

		} catch (Exception e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(String.format("msg=%s, stack=%s", e.getMessage(),
							Arrays.toString(e.getStackTrace()))).build();
		}
	}
}