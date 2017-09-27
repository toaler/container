package container.webapp.jetty;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import container.webapp.api.WebContainer;

public class JettyWebContainer implements WebContainer {

	public void start() {
		try {
			Server server = new Server(8080);
			WebAppContext webAppcontext = new WebAppContext();
			webAppcontext.setResourceBase("src/main/webapp");
			webAppcontext.setContextPath("/");
			webAppcontext.setConfigurations(new Configuration[] { new AnnotationConfiguration() });
			webAppcontext.setParentLoaderPriority(true);

			server.setHandler(webAppcontext);

			server.start();
			server.dumpStdErr();
			server.join();
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

}
