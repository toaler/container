package container.webapp.tomcat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import container.webapp.api.WebContainer;

public class TomcatWebContainer implements WebContainer {

	private static final Logger logger = LoggerFactory.getLogger(TomcatWebContainer.class);

	@Override
	public void start(ApplicationContext acac) {
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);

			// init http connector
			tomcat.getConnector();

			File base = new File(".");
			Context ctx = tomcat.addContext("", base.getAbsolutePath());
			ServletContext servletContext = ctx.getServletContext();

			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException e) {
			throw new RuntimeException("Unable to launch tomcat ", e);
		}
	}

}