package container.webapp.jetty;

import org.eclipse.jetty.server.Server;

import container.webapp.api.WebContainer;

public class JettyWebContainer implements WebContainer {

	public void start() {
		try {
			Server server = new Server(8080);
			server.start();
			server.dumpStdErr();
			server.join();
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

}
