package configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import component.MyComponent;

import service.impl.TimeServiceImpl;

/**
 * Top level configuration responsible for wiring all Spring managed beans together.
 * 
 * @author toal
 *
 */
@Configuration
@Import({TimeServiceImpl.class, MyComponent.class})
public class AppConfig {

}