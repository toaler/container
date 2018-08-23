package resource;

import io.swagger.annotations.Api;
import java.util.Arrays;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.salesforce.healthcheck.HealthCheckService;
import util.Util;

@Component(value = "HealthCheck")
@Path("/healthcheck/status")
@Api
@Consumes({"text/plain"})
@Produces({"text/plain"})
public class Healthcheck {

    @Autowired
    private Util util;

    @Autowired
    @Qualifier("healthCheckService")
    private HealthCheckService hcService;

    @Autowired
    @Qualifier("logger")
    private org.slf4j.Logger logger;

    public Healthcheck() {}

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStatus(@QueryParam("sourcename") String sourceName) {

        try {
            String status = hcService.getStatus();
            logger.info("status={}, sourcename={}", status, sourceName);

            if (status.equals("ALIVE")) {
                return Response.status(Response.Status.OK).entity("ALIVE").build();
            } else if (status.equals("DEAD")) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("DEAD").build();
            }

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error due to unhandled healthcheck status = " + status)
                    .build();

        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("msg=%s, stack=%s", e.getMessage(),
                            Arrays.toString(e.getStackTrace()))).build();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateStatus(String status) {

        try {
            String prevStatus = hcService.getStatus();
            hcService.setStatus(status);
            logger.info(String.format("Updated status from %s to %s", prevStatus, status));
            return Response.status(Response.Status.OK).entity("Updated status to " + status)
                    .build();
        } catch (Exception e) {
            String msg =
                    String.format("fid=%s, msg=Failed to update status to '%s'", util.getFid(e),
                            status);
            logger.error(msg, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
        }
    }
}
