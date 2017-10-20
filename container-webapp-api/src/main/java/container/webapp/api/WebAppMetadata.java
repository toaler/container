package container.webapp.api;

import java.io.File;

public interface WebAppMetadata {
	String getContextPath();
	File getWar();
	
}
