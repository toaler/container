package container.driver.configuration;

import container.webapp.api.WebContainer;
import container.webapp.jetty.JettyWebContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfiguration {

	@Bean(name = "Jetty")
	public WebContainer getJetty() {
		return new JettyWebContainer();
	}
}