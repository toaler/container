package container.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

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
                logger.error("war file not provided, exiting");
                System.exit(1);
            }
            Driver driver = new Driver(LoggerFactory.getLogger(Driver.class), args[0]);  
            driver.start();
        } catch (Exception e) {
            logger.error("Exception occured in main, exiting.", e);
        }
    }
}
