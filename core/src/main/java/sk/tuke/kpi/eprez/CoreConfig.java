package sk.tuke.kpi.eprez;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
public class CoreConfig {

	@Configuration
	@PropertySource(value = { "classpath:/application.properties" })
	public static class ApplicationPropertiesConfig {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreConfig.class);

	@Autowired
	Environment env;

	@PostConstruct
	public void init() throws IOException {
		LOGGER.info("CoreConfig starting with profiles: " + Arrays.asList(env.getActiveProfiles()));
//		SLF4JBridgeHandler.install();
//		LoggerFactory.getLogger(CoreConfig.class).info("Test message from SLF4J");
//		org.apache.log4j.Logger.getLogger(CoreConfig.class).info("Test message from LOG4J");
//		java.util.logging.Logger.getLogger(CoreConfig.class.getName()).info("Test message from JUL");
	}

	public static @Bean PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
