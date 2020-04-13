package container.driver.configuration;


import container.webapp.jetty.JettyWebContainer;
import container.webapp.jetty.MetricsListener;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JettyWebContainer.class, MetricsListener.class})
public class JettyConfiguration {
	
}