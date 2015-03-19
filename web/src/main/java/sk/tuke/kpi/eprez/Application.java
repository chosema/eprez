package sk.tuke.kpi.eprez;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
@Import({ CoreConfig.class })
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static final String ENCODING = "UTF-8";

	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_TOMCAT = "tomcat";
	public static final String PROFILE_OPENSHIFT = "openshift";

	@Autowired
	Environment env;

	@PostConstruct
	public void init() throws IOException {
		LOGGER.info("Application starting with profiles: " + Arrays.asList(env.getActiveProfiles()));
//		SLF4JBridgeHandler.install();
//		LoggerFactory.getLogger(Application.class).info("Test message from SLF4J");
//		org.apache.log4j.Logger.getLogger(Application.class).info("Test message from LOG4J");
//		java.util.logging.Logger.getLogger(Application.class.getName()).info("Test message from JUL");
	}

}
