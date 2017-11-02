package rest.config;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@ApplicationPath("rest")
public class JerseyResourceConfig extends ResourceConfig {

	public void JerseyAppConfiguration(@Context ServletContext context) {
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(context);
		register(appCtx.getBean("TimeRestApi"));
	}
}