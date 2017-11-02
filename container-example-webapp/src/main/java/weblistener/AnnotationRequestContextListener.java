package weblistener;

import javax.servlet.annotation.WebListener;

import org.springframework.web.context.request.RequestContextListener;

/**
 * Used to get {@link RequestContextListener} to get loaded via a annotation,
 * avoiding having to updating web.xml.
 * 
 * @author toal
 *
 */
//@WebListener
public class AnnotationRequestContextListener {
//	extends RequestContextListener {
//}

	public AnnotationRequestContextListener() {
	}
}