package container.driver;

import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		
		logger.info("Starting " + Main.class.getSimpleName());

		for (ClassLoader cl = Main.class.getClassLoader(); cl != null; cl = cl.getParent()) {
			for (URL url : ((URLClassLoader) cl).getURLs()) {
				URL[] urls = ((URLClassLoader) cl).getURLs();
				logger.info(cl.getClass().getName() + " classpath = " + url.getFile());
			}
		}

		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
		acac.scan("container.driver.configuration");
		acac.refresh();

		WebContainer wc = (WebContainer) acac.getBean("Jetty");
		WebAppMetadata metadata = (WebAppMetadata) acac.getBean("WebAppMetadata");
		
		wc.start(metadata, acac);
	}

}