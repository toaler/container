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
	
	public WebAppMetadataImpl(final File war) {
		this.war = war;
		
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

}
