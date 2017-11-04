package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

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
	
	@Bean
	@Scope("prototype")
	Logger logger(InjectionPoint injectionPoint){
		return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
	}
}