package configuration;

import container.webapp.api.WebContainer;
import container.webapp.tomcat.TomcatWebContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {

	@Bean(name = "Tomcat")
	public WebContainer getTomcat() {
		return new TomcatWebContainer();
	}
}