package container.webapp.api;

import org.springframework.context.ApplicationContext;

public interface WebContainer {
	void start(WebAppMetadata meta, ApplicationContext acac);
}
