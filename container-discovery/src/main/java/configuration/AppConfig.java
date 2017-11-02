package configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import components.ServiceRegistry;
import resource.Registration;

@Configuration
@Import({ ServiceRegistry.class, Registration.class })
public class AppConfig {

}