package container.driver;

import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST); // Root logger, for example.

        logger.info("Starting " + Main.class.getSimpleName());

        String war = args[0];
        File warFile = new File(war);
        if (warFile.exists() && !warFile.isDirectory()) {
            logger.info("Loading war = " + war);
        }

        for (ClassLoader cl = Main.class.getClassLoader(); cl != null; cl = cl.getParent()) {
            for (URL url : ((URLClassLoader) cl).getURLs()) {
                logger.info(cl.getClass().getName() + " classpath = " + url.getFile());
            }
        }

        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
        acac.scan("container.driver.configuration");
        acac.refresh();

        WebAppMetadata metadata =
                new WebAppMetadataImpl(warFile,
                        System.getProperty("wc.context.path", getContextMapping(warFile)));

        WebContainer wc = (WebContainer) acac.getBean("Jetty");
        wc.start(metadata, acac);
    }

    private static String getContextMapping(File warFile) {
        Matcher matcher = Pattern.compile("^\\D*(\\d)").matcher(warFile.getName());
        matcher.find();
        String w = matcher.group();
        String contextPath = "/" + w.substring(0, w.length() - 2);
        return contextPath;
    }

}
