package sk.tuke.kpi.eprez.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
public class MessageSourceConfig {

	@Autowired
	Environment env;

	@Bean
	public MessageSource messageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding(Application.ENCODING);
		messageSource.setBasename("messages");
		messageSource.setFallbackToSystemLocale(false);
		if (env.acceptsProfiles(Application.PROFILE_DEV)) {
			messageSource.setCacheSeconds(0);
		} else {
			messageSource.setCacheSeconds(-1);
		}
		return messageSource;
	}

}
