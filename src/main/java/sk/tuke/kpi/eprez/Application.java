package sk.tuke.kpi.eprez;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static final String ENCODING = "UTF-8";

	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_TEST = "test";
	public static final String PROFILE_PROD = "prod";

	@Autowired
	Environment env;

	@PostConstruct
	public void init() {
		LOGGER.info("Application starting with profiles: " + Arrays.asList(env.getActiveProfiles()));
	}

}
