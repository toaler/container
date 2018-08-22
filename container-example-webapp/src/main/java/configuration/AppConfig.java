package configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import com.salesforce.healthcheck.HealthCheckService;
import com.salesforce.healthcheck.HealthCheckServiceImpl;
import component.MyComponent;
import resource.Healthcheck;

/**
 * Top level configuration responsible for wiring all Spring managed beans together.
 * 
 * @author toal
 *
 */
@Configuration
@Import({MyComponent.class, Healthcheck.class})
public class AppConfig {

    @Lazy
    @Bean("logger")
    @Scope("prototype")
    public Logger getLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Bean("healthCheckService")
    public HealthCheckService getHealthCheckService() {
        return new HealthCheckServiceImpl();
    }
}