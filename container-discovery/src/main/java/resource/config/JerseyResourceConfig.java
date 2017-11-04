package resource.config;


import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.jaxrs.config.BeanConfig;

@ApplicationPath("/v1")
@SwaggerDefinition(tags = { @Tag(name = "Registration", description = "Registration operations") })
public class JerseyResourceConfig extends ResourceConfig {

	public JerseyResourceConfig(@Context ServletContext context) {
		
		System.out.println("JerseyResourceConfig");
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(context);
		register(appCtx.getBean("Registration"));

		packages("resource");

		register(io.swagger.jaxrs.listing.ApiListingResource.class);
		register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setBasePath("/v1");
		beanConfig.setVersion("0.0.1");
		beanConfig.setTitle("Container Discovery");
		beanConfig.setTitle("Implementation of Envoy SDS API");
		beanConfig.setResourcePackage("resource");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
	}
}