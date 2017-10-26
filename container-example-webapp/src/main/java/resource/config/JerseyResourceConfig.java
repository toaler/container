package resource.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class JerseyResourceConfig extends ResourceConfig {

  public JerseyResourceConfig() {
	  packages("resource");
  }
}