package resource.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.jaxrs.config.BeanConfig;


@ApplicationPath("rest")
@SwaggerDefinition(tags = { @Tag(name = "Registration", description = "Registration operations") })
public class JerseyResourceConfig extends ResourceConfig {

  public JerseyResourceConfig() {
	  packages("resource");

      register(io.swagger.jaxrs.listing.ApiListingResource.class);
      register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
	  
      BeanConfig beanConfig = new BeanConfig();
      beanConfig.setBasePath("/container-discovery/rest");
      beanConfig.setVersion("0.0.1");
      beanConfig.setTitle("Container Discovery");
      beanConfig.setTitle("Implementation of Envoy SDS API");
      beanConfig.setResourcePackage("resource");
      beanConfig.setPrettyPrint(true);
      beanConfig.setScan(true);
  }
}