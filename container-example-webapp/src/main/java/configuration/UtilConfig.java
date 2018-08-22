package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import util.Util;

@Configuration
public class UtilConfig {

    @Bean
    public Util getUtil() {
        return new Util();
    }
}
