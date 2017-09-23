package configuration;

import container.webapp.api.WebContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import container.webapp.tomcat.TomcatWebContainer;

@Configuration
public class TomcatConfiguration {

	@Bean(name = "Tomcat")
	public WebContainer getTomcat() {
		return new TomcatWebContainer();
	}
}