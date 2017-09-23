package container.driver;

import container.webapp.api.WebContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
		acac.scan("configuration");
		acac.refresh();

		WebContainer wc = (WebContainer) acac.getBean("Jetty");
		wc.start();
	}
}