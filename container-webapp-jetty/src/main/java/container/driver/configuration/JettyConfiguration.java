package container.driver.configuration;


import container.webapp.jetty.JettyWebContainer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JettyWebContainer.class)
public class JettyConfiguration {
	
}