package container.webapp.api;

import org.springframework.context.ApplicationContext;

public interface WebContainer {
	void start(String contextPath, String war, ApplicationContext acac);
}
