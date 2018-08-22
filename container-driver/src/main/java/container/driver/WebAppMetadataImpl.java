package container.driver;

import java.io.File;
import container.webapp.api.WebAppMetadata;

public class WebAppMetadataImpl implements WebAppMetadata {

    private final File war;
    private final String contextMapping;



    public WebAppMetadataImpl(final File war, String contextMapping) {
        this.war = war;
        this.contextMapping = contextMapping;
    }

    @Override
    public File getWar() {
        return war;
    }
    
    @Override
    public String getContextPath() {
        return contextMapping;
    }

    @Override
    public String toString() {
        return "WebAppMetadataImpl [war=" + war + "]";
    }
}
