package container.driver;

import container.webapp.api.WebContainer;

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
		acac.scan("configuration");
		acac.refresh();

		String contextPath = "/example";
		String war = "/home/toal/git/container/container-example-webapp/target/container-example-webapp-0.0.1-SNAPSHOT.war";
		
		WebContainer wc = (WebContainer) acac.getBean("Tomcat");
		wc.start(contextPath, war, acac);
	}

}