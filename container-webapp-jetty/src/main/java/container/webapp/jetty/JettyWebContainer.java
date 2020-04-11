package container.webapp.jetty;

import java.net.URL;
import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NegotiatingServerConnectionFactory;
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


    @Override
    public void start(WebAppMetadata metadata, ApplicationContext acac) {

        try {
            logger.info("[startup] - starting Jetty application container");

            Server server = new Server();

            // Setup HTTP Connector
            HttpConfiguration httpConf = new HttpConfiguration();
            httpConf.setSecurePort(httpsOnePort);
            httpConf.setSecureScheme("https");
            httpConf.setSendXPoweredBy(true);
            httpConf.setSendServerVersion(true);



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
            sslContextFactory.setKeyManagerPassword("jettyjetty");
            sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
            
            // Establish the HTTP ServerConnector
            ServerConnector httpConnector =
                    new ServerConnector(server, new HttpConnectionFactory(httpConf));
            httpConnector.setPort(httpOnePort);
            httpConnector.setIdleTimeout(5000);
            server.addConnector(httpConnector);
            
            // Establish the HTTPS ServerConnector
            ServerConnector httpsConnector =
                    new ServerConnector(server, sslContextFactory, new HttpConnectionFactory(httpConf));
            httpsConnector.setPort(httpsOnePort);
            httpsConnector.setIdleTimeout(5000);
            server.addConnector(httpsConnector);

            // Setup HTTPS Configuration
            HttpConfiguration httpsConf = new HttpConfiguration(httpConf);
            httpsConf.addCustomizer(new SecureRequestCustomizer()); // adds ssl
                                                                    // info
                                                                    // to
                                                                    // request
                                                                    // object

            // HTTP/2 Connection Factory
            HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConf);

 //           NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
            ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
            alpn.setDefaultProtocol(httpConnector.getDefaultProtocol());

            // SSL Connection Factory
            SslConnectionFactory ssl =
                    new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

            // HTTP/2 Connector
            ServerConnector http2Connector =
                    new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(httpsConf));
            http2Connector.setPort(httpTwoPort);
            ALPN.debug = true;
            server.addConnector(http2Connector);

            WebAppContext context = new WebAppContext();
            context.setContextPath(metadata.getContextPath());
            context.setWar(metadata.getWar().getAbsolutePath());
            context.setExtractWAR(true);

            context.setConfigurations(new Configuration[] {new AnnotationConfiguration(),
                    new WebInfConfiguration(), new WebXmlConfiguration(),
                    new MetaInfConfiguration(), new FragmentConfiguration(),
                    new EnvConfiguration(), new PlusConfiguration(), new JettyWebXmlConfiguration()});

            String url =
                    JettyWebContainer.class.getProtectionDomain().getCodeSource().getLocation()
                            .toString();

            String a = ".*/^(asm-all-repackaged)[^/]*\\.jar";
            context.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern", a);

            String jarRegex =
                    ".*" + Files.getNameWithoutExtension(url) + "\\." + Files.getFileExtension(url);
            context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                    jarRegex);
            context.setParentLoaderPriority(true);


            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] {
                    new ShutdownHandler((String) acac.getBean("shutdownToken")), context});
            server.setHandler(handlers);

            
            server.start();

            logger.info("[startup] - started Jetty application container");

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

            server.join();

        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
