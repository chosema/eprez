package sk.tuke.kpi;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
public class MessageSourceConfig {

	@Resource
	private Environment env;

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
