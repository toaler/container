package container.driver.configuration;

import container.webapp.jetty.JettyWebContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(JettyWebContainer.class)
public class JettyConfiguration {

//	@Bean(name = "Jetty")
//	public WebContainer getJetty() {
//		return new JettyWebContainer();
//	}
	
	@Bean
	@Scope("prototype")
	Logger logger(InjectionPoint injectionPoint){
		return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
	}
}