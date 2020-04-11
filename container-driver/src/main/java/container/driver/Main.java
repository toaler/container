package container.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String CONTAINER_TYPE_BEAN_NAME = System.getProperty("container.driver.type.bean.name", "Jetty");

	public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);

        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Thread") {
        @Override
            public void run() {
                logger.info("Shutting down.");
            }   
        }); 
        
        try {
            if (args.length == 0) {
                logger.error("War file not provided, exiting");
                System.exit(1);
            }

            if (!args[0].contains(".war")) {
                logger.error("The war filename provided '" + args[0] + "' isn't a war, exiting");
                System.exit(1);
            }
            
            logger.info("War file provided = '" + args[0] + "'");
            Driver driver = new Driver(LoggerFactory.getLogger(Driver.class), args[0]);  
            driver.start(CONTAINER_TYPE_BEAN_NAME);
        } catch (Exception e) {
            logger.error("Exception occured in main, exiting.", e);
        }
    }
}
