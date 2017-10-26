package container.webapp.tomcat;

import java.io.File;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;

public class TomcatWebContainer implements WebContainer {

	private static final Logger logger = LoggerFactory.getLogger(TomcatWebContainer.class);

	@Override
	public void start(WebAppMetadata metadata, ApplicationContext acac) {

		try {
			StringBuilder sb = new StringBuilder();

			sb.append("___________                           __   \n");
			sb.append("\\__    ___/___   _____   ____ _____ _/  |_ \n");
			sb.append("  |    | /  _ \\ /     \\_/ ___\\\\__  \\\\   __\\\n");
			sb.append("  |    |(  <_> )  Y Y  \\  \\___ / __ \\|  |  \n");
			sb.append("  |____| \\____/|__|_|  /\\___  >____  /__|  \n");
			sb.append("                     \\/     \\/     \\/      \n");

			System.out.println(sb.toString());

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

			tomcat.addWebapp(host, metadata.getContextPath(), metadata.getWar().getAbsolutePath());

			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException | SecurityException | IllegalArgumentException e) {
			throw new RuntimeException("Unable to launch tomcat ", e);
		}
	}
}