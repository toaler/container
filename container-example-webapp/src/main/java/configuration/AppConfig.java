package configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import component.MyComponent;
import service.impl.HealthCheckServiceImpl;
import service.impl.TimeServiceImpl;

/**
 * Top level configuration responsible for wiring all Spring managed beans together.
 * 
 * @author toal
 *
 */
@Configuration
@Import({TimeServiceImpl.class, MyComponent.class, HealthCheckServiceImpl.class})
public class AppConfig {
	
	@Lazy
	@Bean("logger")
	@Scope("prototype")
	public Logger getLogger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
	
}