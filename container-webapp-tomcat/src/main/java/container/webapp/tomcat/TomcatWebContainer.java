package container.webapp.tomcat;

import org.apache.catalina.startup.Tomcat;

import container.webapp.api.WebContainer;

public class TomcatWebContainer implements WebContainer {

	public void start() {

		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);

			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

}
