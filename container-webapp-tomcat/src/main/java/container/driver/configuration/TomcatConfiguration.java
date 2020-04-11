package container.driver.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import container.webapp.tomcat.TomcatWebContainer;

@Configuration
@Import(TomcatWebContainer.class)
public class TomcatConfiguration {
	
}