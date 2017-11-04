package container.driver;

import java.io.File;

import container.webapp.api.WebAppMetadata;

public class WebAppMetadataImpl implements WebAppMetadata {
	@Override
	public String toString() {
		return "WebAppMetadataImpl [contextPath=" + contextPath + ", war=" + war + "]";
	}

	private final String contextPath;
	private final File war;
	private final int port;
	
	public WebAppMetadataImpl(final File war, final int port, String contextPath) {
		this.war = war;
		this.port = port;
		this.contextPath = contextPath;

	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public File getWar() {
		return war;
	}

	@Override
	public int getPort() {
		return port;
	}

}
