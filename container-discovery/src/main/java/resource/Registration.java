package resource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.ServiceInstance;
import components.ServiceRegistry;
import components.Tags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Component(value = "Registration")
@Path("/v1/registration/{service}")
@Api(tags = "Registration")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class Registration {
	@Autowired
	private ServiceRegistry registry;

	@POST
	@ApiOperation(value = "Registers a service with discovery", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void registerService(
			@ApiParam(name = "service", value = "Service for which operation is performed.", required = true) @PathParam("service") final String service,
			@ApiParam(name = "ip", value = "ip address of the host.", required = true) @QueryParam("ip") final String ip,
			@ApiParam(name = "service_repo_name", value = "service repository name, can be used for quick search", required = false) @QueryParam("service_repo_name") final String serviceRepoName,
			@ApiParam(name = "port", value = "port on which the host expects connections, Envoy will connect to this port",  required = true) @QueryParam("port") final Integer port,
			@ApiParam(name = "revision",  value = "SHA of the revision the service currently running", required = true) @QueryParam("revision") final String revision,
			@ApiParam(name = "service_params", value = "JSON in the following format, see https://github.com/lyft/discovery#tags-json for expections", required = true) @QueryParam("service_params") String json) throws JsonParseException, JsonMappingException, IOException {

			ObjectMapper mapper = new ObjectMapper();
			Tags tags = mapper.readValue(json, Tags.class);
			ServiceInstance instance = new ServiceInstance(ip, serviceRepoName, port, revision, tags);
			registry.add(service, instance);
	}
}