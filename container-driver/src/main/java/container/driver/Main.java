package container.driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import container.driver.util.Util;
import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {
        String pidFile = null;
        try {
            
            if (args.length == 0) {
                logger.error("war file not provided, exiting");
                System.exit(1);
            }

            installSLF4JBridgeHandler();
            logger.info("Starting " + Main.class.getSimpleName());

            WebAppMetadata metadata = createWebAppMetatdata(args);

            pidFile = createPidFile(metadata.getWebAppName());
            if (pidFile.equals("")) {
                System.exit(1);
            }

            for (ClassLoader cl = Main.class.getClassLoader(); cl != null; cl = cl.getParent()) {
                logger.info(cl.getClass().getName() + " classpath entries:");
                for (URL url : ((URLClassLoader) cl).getURLs()) {
                    logger.info(url.getFile());
                }
            }

            AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
            acac.scan("container.driver.configuration");
            acac.refresh();

            WebContainer wc = (WebContainer) acac.getBean("Jetty");
            wc.start(metadata, acac);
        } catch (Exception e) {
           logger.error("Exception occured in main, exiting.", e);
        }
    }

    private static WebAppMetadata createWebAppMetatdata(String[] args) {
        String war = args[0];
        File warFile = new File(war);
        String name = warFile.getName();

        String webAppName = name.substring(0, name.lastIndexOf("."));

        if (warFile.exists() && !warFile.isDirectory()) {
            logger.info("Loading war = " + war);
        }

        WebAppMetadata metadata =
                new WebAppMetadataImpl(webAppName, warFile, System.getProperty("wc.context.path",
                        getContextMapping(warFile)));
        return metadata;
    }

    private static String createPidFile(String webAppName) {
        Util util = new Util();
        int pid = util.getPid();

        String pidFile = System.getProperty("pid.file", String.format("/tmp/%s.pid", webAppName));
        try (FileWriter w = new FileWriter(pidFile)) {
            util.writePid(w);
            logger.info("Pidfile created successfully (pidfile={} with pid={})", pidFile, pid);
            return pidFile;
        } catch (IOException e) {
            logger.error(String.format("Pidfile %s failed to be created pidfile=", pidFile), e);
            return "";
        }
    }

    private static void installSLF4JBridgeHandler() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST); // Root logger, for example.
    }

    private static String getContextMapping(File warFile) {
        Matcher matcher = Pattern.compile("^\\D*(\\d)").matcher(warFile.getName());
        matcher.find();
        String w = matcher.group();
        String contextPath = "/" + w.substring(0, w.length() - 2);
        return contextPath;
    }
}