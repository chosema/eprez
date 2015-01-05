package sk.tuke.kpi;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static final String ENCODING = "UTF-8";

	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_TEST = "test";
	public static final String PROFILE_PROD = "prod";

	@Resource
	private Environment env;

	@PostConstruct
	public void init() {
		LOGGER.info("Application starting with profiles: " + Arrays.asList(env.getActiveProfiles()));
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return super.configure(application.sources(Application.class));
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
