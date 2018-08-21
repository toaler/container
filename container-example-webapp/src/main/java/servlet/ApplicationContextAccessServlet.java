package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import component.MyComponent;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/applicationcontext" })
public class ApplicationContextAccessServlet extends HttpServlet {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private MyComponent myComponent;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doGet");
		response.setContentType("text/plain");
		response.getWriter().write(new Date().toString() + "\n");

		response.getWriter().write(myComponent.message() + "\n");
	}

}