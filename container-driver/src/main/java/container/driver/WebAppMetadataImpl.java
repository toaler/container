package container.driver;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import container.webapp.api.WebAppMetadata;

public class WebAppMetadataImpl implements WebAppMetadata {
	@Override
	public String toString() {
		return "WebAppMetadataImpl [contextPath=" + contextPath + ", war=" + war + "]";
	}

	private final String contextPath;
	private final File war;
	private final int port;
	
	public WebAppMetadataImpl(final File war, final int port) {
		this.war = war;
		this.port = port;
		
		String filename = war.getName();
		Matcher matcher = Pattern.compile("^\\D*(\\d)").matcher(filename);
		matcher.find();
		String w = matcher.group();
		contextPath = "/" + w.substring(0, w.length() - 2);
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
