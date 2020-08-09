package servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.github.benmanes.caffeine.cache.Cache;

import component.MyComponent;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/applicationcontext" })
public class ApplicationContextAccessServlet extends HttpServlet {

	@Autowired
	private Logger logger;

	@Autowired
	private MyComponent myComponent;

	@Autowired
	private Cache<String, String> eTagCache;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("doGet");

		var providedETag = request.getHeader("If-None-Match");

		if (providedETag != null) {

			String cachedResponse = eTagCache.getIfPresent(providedETag);

			if (cachedResponse != null) {
				response.setStatus(304);
				return;
			}

		}

		response.setContentType("text/html");
		response.setHeader("cache-control", "private, max-age=5");

		var sb = new StringBuilder();
		sb.append(new Date().toString() + "\n");
		sb.append(myComponent.message() + "\n");

		var responseText = sb.toString();
		String eTag = generateEtag(responseText);

		eTagCache.put(eTag, responseText);

		response.setHeader("ETag", eTag);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.getWriter().write(responseText);
	}

	public String generateEtag(String input) {
		var eTag = "";
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(input.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			eTag = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return eTag;
	}

}