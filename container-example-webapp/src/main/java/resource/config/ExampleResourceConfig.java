package resource.config;

import io.swagger.jaxrs.config.BeanConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@ApplicationPath("/v1")
public class ExampleResourceConfig extends ResourceConfig {

    public ExampleResourceConfig(@Context ServletContext context) {

        WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(context);
        register(appCtx.getBean("HealthCheck"));

        register(LoggingFeature.class);

        property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
        property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_SERVER,
                LoggingFeature.Verbosity.HEADERS_ONLY);
        
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setBasePath("/v1");
        beanConfig.setVersion("0.0.1");
        beanConfig.setTitle("Example webapplication");
        beanConfig.setResourcePackage("resource");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }
}
