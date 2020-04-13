package container.webapp.jetty;

import java.io.IOException;
import java.net.URL;
import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.google.common.io.Files;
import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;

@Component("Jetty")
public class JettyWebContainer implements WebContainer {

	private static final String KEY_STORE_PASSWORD = "jettyjetty";

	private static final long IDLE_TIMEOUT = 5000;

	@Autowired
	@Qualifier("logger")
	private org.slf4j.Logger logger;

	@Autowired
	@Qualifier("httpOnePort")
	private Integer httpOnePort;

	@Autowired
	@Qualifier("httpsOnePort")
	private Integer httpsOnePort;

	@Autowired
	@Qualifier("httpTwoPort")
	private Integer httpTwoPort;

	@Autowired
	@Qualifier("shutdownToken")
	private String shutdownToken;

	@Autowired
	@Qualifier("metricsListener")
	private Object metricsListener;

	@Override
	public void start(WebAppMetadata metadata, ApplicationContext acac) {

		try {
			logger.info("[startup] - starting Jetty application container");

			// Setup SSL
			URL f = findSslKeystore();
			SslContextFactory sslContextFactory = createSslContextFactory(f);

			Server server = new Server();

			// Setup configs
			HttpConfiguration httpOneConfig = setupHttpOneConfig();
			HttpConfiguration httpTwoConfig = setupHttpTwoConfig(httpOneConfig);

			// Setup and register HTTP 1.1 connector
			HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpOneConfig);
			ServerConnector httpConnector = createServerConnector(server, httpOnePort, IDLE_TIMEOUT, null,
					httpConnectionFactory);
			server.addConnector(httpConnector);

			// Setup and register HTTP 1.1 TLS connector
			ServerConnector httpsConnector = createServerConnector(server, httpsOnePort, IDLE_TIMEOUT,
					sslContextFactory, httpConnectionFactory);
			server.addConnector(httpsConnector);

			// HTTP/2 Connection Factory
			HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpTwoConfig);

			// ALPN Connection Factory
			ALPNServerConnectionFactory alpn = createAlpnConnectionFactory(httpConnector);

			// SSL Connection Factory
			SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

			// HTTP/2 Connector
			ServerConnector http2Connector = createServerConnector(server, httpTwoPort, IDLE_TIMEOUT, null, ssl, alpn,
					h2, new HttpConnectionFactory(httpTwoConfig));
			ALPN.debug = true;
			server.addConnector(http2Connector);

			WebAppContext context = createWebAppContext(metadata);
			setupHandlers(acac, server, context);

			server.start();

			logger.info("[startup] - started Jetty application container");
			logContainerInfo(server);

			server.join();
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	private HttpConfiguration setupHttpOneConfig() {
		HttpConfiguration httpConf = new HttpConfiguration();
		httpConf.setSecurePort(httpsOnePort);
		httpConf.setSecureScheme("https");
		httpConf.setSendXPoweredBy(true);
		httpConf.setSendServerVersion(true);
		return httpConf;
	}

	private void logContainerInfo(Server server) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n     ____.       __    __          \n");
		sb.append("    |    | _____/  |__/  |_ ___.__.\n");
		sb.append("    |    |/ __ \\   __\\   __<   |  |\n");
		sb.append("/\\__|    \\  ___/|  |  |  |  \\___  |\n");
		sb.append("\\________|\\___  >__|  |__|  / ____|\n");
		sb.append("              \\/            \\/\n");
		sb.append("\nJetty configuration: ");
		server.dump(sb);
		logger.info(sb.toString());
	}

	private ALPNServerConnectionFactory createAlpnConnectionFactory(ServerConnector httpConnector) {
		ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
		alpn.setDefaultProtocol(httpConnector.getDefaultProtocol());
		return alpn;
	}

	private HttpConfiguration setupHttpTwoConfig(HttpConfiguration httpConf) {
		HttpConfiguration config = new HttpConfiguration(httpConf);
		// adds ssl info to request object
		config.addCustomizer(new SecureRequestCustomizer());
		return config;
	}

	private URL findSslKeystore() {
		// Find Keystore for SSL
		ClassLoader cl = JettyWebContainer.class.getClassLoader();
		String keystoreResource = "ssl/keystore";
		URL f = cl.getResource(keystoreResource);
		if (f == null) {
			throw new RuntimeException("Unable to find " + keystoreResource);
		}
		return f;
	}

	private void setupHandlers(ApplicationContext acac, Server server, WebAppContext context) {
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { new ShutdownHandler(shutdownToken), context });
		server.setHandler(handlers);
	}

	private WebAppContext createWebAppContext(WebAppMetadata metadata) {
		WebAppContext context = new WebAppContext();
		context.setContextPath(metadata.getContextPath());
		context.setWar(metadata.getWar().getAbsolutePath());
		context.setExtractWAR(true);

		context.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebInfConfiguration(),
				new WebXmlConfiguration(), new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration(), new PlusConfiguration(), new JettyWebXmlConfiguration() });

		String url = JettyWebContainer.class.getProtectionDomain().getCodeSource().getLocation().toString();

		String a = ".*/^(asm-all-repackaged)[^/]*\\.jar";
		context.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern", a);

		String jarRegex = ".*" + Files.getNameWithoutExtension(url) + "\\." + Files.getFileExtension(url);
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", jarRegex);
		context.setParentLoaderPriority(true);
		return context;
	}

	private ServerConnector createServerConnector(Server server, int port, long timeout,
			SslContextFactory sslContextFactory, ConnectionFactory... factories) {

		ServerConnector httpConnector = null;
		httpConnector = (sslContextFactory == null) ? new ServerConnector(server, factories)
				: new ServerConnector(server, sslContextFactory, factories);

		httpConnector.setPort(port);
		httpConnector.setIdleTimeout(timeout);
		
		httpConnector.addBean(metricsListener);

		return httpConnector;
	}

	private SslContextFactory createSslContextFactory(URL f) {
		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStorePath(f.toExternalForm());
		sslContextFactory.setKeyStorePassword(KEY_STORE_PASSWORD);
		sslContextFactory.setKeyManagerPassword(KEY_STORE_PASSWORD);
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
		return sslContextFactory;
	}
}
