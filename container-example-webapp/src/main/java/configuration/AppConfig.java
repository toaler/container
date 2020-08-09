package configuration;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import component.HealthCheckServiceImpl;
import component.MyComponent;
import resource.Healthcheck;

/**
 * Top level configuration responsible for wiring all Spring managed beans
 * together.
 * 
 * @author toal
 *
 */
@Configuration
@Import({ MyComponent.class, Healthcheck.class, HealthCheckServiceImpl.class })
public class AppConfig {

	@Lazy
	@Bean("logger")
	@Scope("prototype")
	public Logger getLogger(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
	}

	@Lazy
	@Bean("eTagCache")
	public Cache<String, String> getETagCache() {
		Cache<String, String> eTagCache = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS)
				.maximumSize(10_000).build();

		return eTagCache;
	}
}
