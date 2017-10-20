package configuration;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import container.driver.WebAppMetadataImpl;
import container.webapp.api.WebAppMetadata;

@Configuration
public class DriverConfig {

	@Bean(name = "WebAppMetadata")
	public WebAppMetadata getWebAppMetadata() {
		return new WebAppMetadataImpl(
				new File("../container-example-webapp/target/container-example-webapp-0.0.1-SNAPSHOT.war"));
	}
}
