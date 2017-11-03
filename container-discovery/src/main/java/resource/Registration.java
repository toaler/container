package resource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.Environment;
import components.ServiceInstance;
import components.ServiceMetadata;
import components.ServiceRegistry;
import components.ServiceRepoMetadata;
import components.Tags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Component(value = "Registration")
@Path("/v1/registration")
@Api(tags = "Registration")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class Registration {
	@Autowired
	private ServiceRegistry registry;

	@Autowired
	private Environment environment;

	@POST
	@Path("{service}")
	@ApiOperation(value = "Registers a service with discovery", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void registerService(
			@ApiParam(name = "service", value = "Service for which operation is performed.", required = true) @PathParam("service") final String service,
			@ApiParam(name = "ip", value = "ip address of the host.", required = true) @QueryParam("ip") final String ip,
			@ApiParam(name = "service_repo_name", value = "service repository name, can be used for quick search", required = false) @QueryParam("service_repo_name") final String serviceRepoName,
			@ApiParam(name = "port", value = "port on which the host expects connections, Envoy will connect to this port", required = true) @QueryParam("port") final Integer port,
			@ApiParam(name = "revision", value = "SHA of the revision the service currently running", required = true) @QueryParam("revision") final String revision,
			@ApiParam(name = "service_params", value = "JSON in the following format, see https://github.com/lyft/discovery#tags-json for expections", required = true) @QueryParam("service_params") String json)
			throws JsonParseException, JsonMappingException, IOException {

		if (service == null || ip == null || port == null || revision == null || json == null) {
			throw new BadRequestException();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		Tags tags = mapper.readValue(json, Tags.class);
		ServiceInstance instance = new ServiceInstance(service, ip, serviceRepoName, port, revision, tags);
		registry.add(service, instance);
	}

	@GET
	@Path("{service}")
	@ApiOperation(value = "Fetches service instances for the given service", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceMetadata fetchServiceInstances(
			@ApiParam(name = "service", value = "Service for which operation is performed.", required = true) @PathParam("service") final String service) {
		
		if (service == null) {
			throw new BadRequestException();
		}
		
		Set<ServiceInstance> instances = registry.get(service);
		return new ServiceMetadata(environment.getType(), service,
				instances.toArray(new ServiceInstance[instances.size()]));
	}
	
	@GET
	@Path("repo/{service_repo_name}")
	@ApiOperation(value = "Fetches service instances for the given service", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceRepoMetadata fetchServiceInstancesByServiceRepoName(
			@ApiParam(name = "service_repo_name", value = "Service for which operation is performed.", required = true) @PathParam("service_repo_name") final String serviceRepo) {

		if (serviceRepo == null) {
			throw new BadRequestException();
		}

		Set<ServiceInstance> instances = registry.getByRepo(serviceRepo);
		return new ServiceRepoMetadata(environment.getType(), serviceRepo,
				instances.toArray(new ServiceInstance[instances.size()]));
	}
	
	
}