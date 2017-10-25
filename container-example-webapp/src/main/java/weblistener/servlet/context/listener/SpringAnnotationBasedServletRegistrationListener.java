package weblistener.servlet.context.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@WebListener
public class SpringAnnotationBasedServletRegistrationListener implements ServletContextListener {
	private final ContextLoader contextLoader = new ContextLoader();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println(SpringAnnotationBasedServletRegistrationListener.class.getSimpleName() + " ");
		ServletContext servletContext = event.getServletContext();
		servletContext.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
		servletContext.setInitParameter("contextConfigLocation", "configuration");
		
		WebApplicationContext context = contextLoader.initWebApplicationContext(servletContext);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
}