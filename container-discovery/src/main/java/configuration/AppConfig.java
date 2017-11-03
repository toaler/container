package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import components.Environment;
import components.ServiceRegistry;
import resource.Registration;

@Configuration
@Import({ ServiceRegistry.class, Registration.class })
public class AppConfig {
	
	@Bean
	public Environment getEnvironment() {
		return new Environment("prod");
	}
}