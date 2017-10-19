package container.webapp.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TomcatWebContainer {

	private static final Logger logger = LoggerFactory.getLogger(TomcatWebContainer.class);

	public static void main(String args[]) {

		try {
			Tomcat tomcat = new Tomcat();

			File docBase = new File(System.getProperty("java.io.tmpdir"));
			tomcat.setBaseDir(docBase.getAbsolutePath());

			tomcat.setSilent(false);
			tomcat.setPort(8080);

			// init http connector
			tomcat.getConnector();

			// Look for that variable and default to 8080 if it isn't there.

			String webPort = System.getenv("PORT");
			if (webPort == null || webPort.isEmpty()) {
				webPort = "8080";
			}

			tomcat.setPort(Integer.valueOf(webPort));

			tomcat.setBaseDir("/");

			Host host = tomcat.getHost();
			host.setAppBase("/tmp");
			host.setAutoDeploy(true);
			host.setDeployOnStartup(true);

			Context c = tomcat.addWebapp(host, "/example",
					new File(
							"/home/toal/git/container/container-example-webapp/target/container-example-webapp-0.0.1-SNAPSHOT.war")
									.getAbsolutePath());

			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException | SecurityException | IllegalArgumentException e) {
			throw new RuntimeException("Unable to launch tomcat ", e);
		}
	}

}