package container.webapp.jetty;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NegotiatingServerConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.springframework.context.ApplicationContext;

import com.google.common.io.Files;

import container.webapp.api.WebContainer;

public class JettyWebContainer implements WebContainer {

	@Override
	public void start(String contextPath, String war, ApplicationContext acac) {

		try {

			Log.setLog(new StdErrLog());

			StringBuilder sb = new StringBuilder();
			sb.append("     ____.       __    __          \n");
			sb.append("    |    | _____/  |__/  |_ ___.__.\n");
			sb.append("    |    |/ __ \\   __\\   __<   |  |\n");
			sb.append("/\\__|    \\  ___/|  |  |  |  \\___  |\n");
			sb.append("\\________|\\___  >__|  |__|  / ____|\n");
			sb.append("              \\/            \\/\n");

			System.out.println(sb.toString());

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			Server server = new Server();

			int httpPort = 8080;
			int httpsPort = 8443;

			// Setup HTTP Connector
			HttpConfiguration httpConf = new HttpConfiguration();
			httpConf.setSecurePort(httpsPort);
			httpConf.setSecureScheme("https");
			httpConf.setSendXPoweredBy(true);
			httpConf.setSendServerVersion(true);

			// Establish the HTTP ServerConnector
			ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(httpConf));
			httpConnector.setPort(httpPort);
			httpConnector.setIdleTimeout(5000);
			server.addConnector(httpConnector);

			// Find Keystore for SSL
			ClassLoader cl = JettyWebContainer.class.getClassLoader();
			String keystoreResource = "ssl/keystore";
			URL f = cl.getResource(keystoreResource);
			if (f == null) {
				throw new RuntimeException("Unable to find " + keystoreResource);
			}

			// Setup SSL
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(f.toExternalForm());
			sslContextFactory.setKeyStorePassword("jettyjetty");
			sslContextFactory.setKeyManagerPassword("storepwd");
			sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

			// Setup HTTPS Configuration
			HttpConfiguration httpsConf = new HttpConfiguration(httpConf);
			httpsConf.addCustomizer(new SecureRequestCustomizer()); // adds ssl
																	// info
																	// to
																	// request
																	// object

			// HTTP/2 Connection Factory
			HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConf);

			NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
			ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
			alpn.setDefaultProtocol(httpConnector.getDefaultProtocol());

			// SSL Connection Factory
			SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

			// HTTP/2 Connector
			ServerConnector http2Connector = new ServerConnector(server, ssl, alpn, h2,
					new HttpConnectionFactory(httpsConf));
			http2Connector.setPort(httpsPort);
			ALPN.debug = true;
			server.addConnector(http2Connector);

			WebAppContext context = new WebAppContext();
			context.setContextPath(contextPath);
			context.setWar(war);
			context.setExtractWAR(true);

			context.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebInfConfiguration(),
					new WebXmlConfiguration(), new MetaInfConfiguration(), new FragmentConfiguration(),
					new EnvConfiguration(), new PlusConfiguration(), new JettyWebXmlConfiguration() });

			String url = JettyWebContainer.class.getProtectionDomain().getCodeSource().getLocation().toString();
			String jarRegex = ".*" + Files.getNameWithoutExtension(url) + "\\." + Files.getFileExtension(url);
			context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", jarRegex);
			context.setParentLoaderPriority(true);

			server.setHandler(context);

			server.start();

			server.dump(System.err);

			server.join();

		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	private static URI findWebResourceBase(ClassLoader classLoader) {
		String webResourceRef = "WEB-INF/web.xml";

		try {
			// Look for resource in classpath (best choice when working with
			// archive jar/war file)
			URL webXml = classLoader.getResource('/' + webResourceRef);
			if (webXml != null) {
				URI uri = webXml.toURI().resolve("..").normalize();
				System.err.printf("WebResourceBase (Using ClassLoader reference) %s%n", uri);
				return uri;
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException("Bad ClassPath reference for: " + webResourceRef, e);
		}

		// Look for resource in common file system paths
		try {
			Path pwd = new File(System.getProperty("user.dir")).toPath().toRealPath();
			FileSystem fs = pwd.getFileSystem();

			// Try the generated maven path first
			PathMatcher matcher = fs.getPathMatcher("glob:**/embedded-servlet-*");
			try (DirectoryStream<Path> dir = java.nio.file.Files.newDirectoryStream(pwd.resolve("target"))) {
				for (Path path : dir) {
					if (java.nio.file.Files.isDirectory(path) && matcher.matches(path)) {
						// Found a potential directory
						Path possible = path.resolve(webResourceRef);
						// Does it have what we need?
						if (java.nio.file.Files.exists(possible)) {
							URI uri = path.toUri();
							System.err.printf("WebResourceBase (Using discovered /target/ Path) %s%n", uri);
							return uri;
						}
					}
				}
			}

			// Try the source path next
			Path srcWebapp = pwd.resolve("src/main/webapp/" + webResourceRef);
			if (java.nio.file.Files.exists(srcWebapp)) {
				URI uri = srcWebapp.getParent().toUri();
				System.err.printf("WebResourceBase (Using /src/main/webapp/ Path) %s%n", uri);
				return uri;
			}
		} catch (Throwable t) {
			throw new RuntimeException("Unable to find web resource in file system: " + webResourceRef, t);
		}

		throw new RuntimeException("Unable to find web resource ref: " + webResourceRef);
	}
}
