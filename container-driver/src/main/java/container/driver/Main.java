package container.driver;

import container.webapp.api.WebContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		logger.info("Starting " + Main.class.getSimpleName());

		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
		acac.scan("configuration");
		acac.refresh();

		WebContainer wc = (WebContainer) acac.getBean("Jetty");
		wc.start();
	}
}