package weblistener.request.context.listener;

import javax.servlet.annotation.WebListener;

import org.springframework.web.context.request.RequestContextListener;

@WebListener
public class AnnotationRequestContextListener extends RequestContextListener {
	
	public AnnotationRequestContextListener() {
		System.out.println("AnnotationRequestContextListener ctor");
	}
}