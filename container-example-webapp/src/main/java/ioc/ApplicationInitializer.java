package ioc;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


/**
 * Get's called by {@link SpringServletContainerInitializer} which is a
 * {@link ServletContainerInitializer}.
 * 
 * @author btoal
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        servletContext.addListener(new ContextLoaderListener(context));

        // Set's the list of packages that the ApplicationContext will search to find registered
        // Spring managed beans
        servletContext.setInitParameter("contextConfigLocation", "configuration");
    }
}
