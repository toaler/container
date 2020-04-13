package container.driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import container.driver.util.Util;
import container.webapp.api.WebAppMetadata;
import container.webapp.api.WebContainer;

public class Driver {
    private final Logger logger;
    private final String war;

    public Driver(Logger logger, String war) {
        this.logger = logger;
        this.war = war;
    }

    public void start(String containerBeanName) {
    	
    	try {

        installSLF4JBridgeHandler();
        logger.info("Starting " + Main.class.getSimpleName());

        WebAppMetadata metadata = createWebAppMetatdata(war);

        String pidFile = createPidFile(metadata.getWebAppName());
        if (pidFile.equals("")) {
            System.exit(1);
        }

		logger.info("classpath entries:");

		String pathSeparator = System.getProperty("path.separator");
		String[] classPathEntries = System.getProperty("java.class.path").split(pathSeparator);

		for (String entry : classPathEntries) {
			logger.info(entry);
		}

        AnnotationConfigApplicationContext acac = startIoc();
        WebContainer wc = (WebContainer) acac.getBean(containerBeanName);
        wc.start(metadata, acac);
        
    	} catch (Throwable t) {
    		logger.error("Error occured when attempting to start conatiner", t);
    		throw t;
    	}
    }


    private AnnotationConfigApplicationContext startIoc() {
        logger.info("[startup] - starting Spring IoC");
        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
        acac.scan("container.driver.configuration");
        acac.refresh();
        dumpIocBanner();
        logger.info("[startup] - started Spring IoC");
        return acac;
    }

    private void dumpIocBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  _________            .__               \n");
        sb.append(" /   _____/____________|__| ____    ____\n");
        sb.append(" \\_____  \\\\____ \\_  __ \\  |/    \\  / ___\\\n");
        sb.append(" /        \\  |_> >  | \\/  |   |  \\/ /_/  >\n");
        sb.append("/_______  /   __/|__|  |__|___|  /\\___  /\n");
        sb.append("        \\/|__|                 \\//_____/\n");
        
        logger.info(sb.toString());

    }

    private WebAppMetadata createWebAppMetatdata(String war) {
        File warFile = new File(war);
        String name = warFile.getName();
        logger.info("warFile = " + name);

        String webAppName = name.substring(0, name.lastIndexOf("."));

        if (warFile.exists() && !warFile.isDirectory()) {
            logger.info("Loading war = " + war);
        }

        WebAppMetadata metadata =
                new WebAppMetadataImpl(webAppName, warFile, System.getProperty("wc.context.path",
                        getContextMapping(warFile)));
        return metadata;
    }

    private String createPidFile(String webAppName) {
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

    private void installSLF4JBridgeHandler() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST); // Root logger, for example.
    }

    private String getContextMapping(File warFile) {
        Matcher matcher = Pattern.compile("^\\D*(\\d)").matcher(warFile.getName());
        matcher.find();
        String w = matcher.group();
        String contextPath = "/" + w.substring(0, w.length() - 2);
        return contextPath;
    }

}
