package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import component.MyComponent;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/applicationcontext" })
public class ApplicationContextAccessServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		response.getWriter().write(new Date().toString() + "\n");

		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		MyComponent c = (MyComponent) wac.getBean("MyComponent");
		response.getWriter().write(c.message() + "\n");
		response.getWriter().write(downCall() + "\n");
	}

	private String downCall() {
		ServletContext sc = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);

		MyComponent c = (MyComponent) wac.getBean("MyComponent");
		return c.message();
	}
}