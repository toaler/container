package container.webapp.api;

import java.io.File;

public interface WebAppMetadata {
	File getWar();

    String getContextPath();

    String getWebAppName();
}
