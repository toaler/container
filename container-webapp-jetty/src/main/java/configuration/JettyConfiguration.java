package configuration;

import container.webapp.api.WebContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import container.webapp.jetty.JettyWebContainer;

@Configuration
public class JettyConfiguration {

	@Bean(name = "Jetty")
	public WebContainer getJetty() {
		return new JettyWebContainer();
	}
}