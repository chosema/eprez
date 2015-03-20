package sk.tuke.kpi.eprez.web;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScopesConfig {

	@Bean
	// must be static because it is returning BeanFactoryPostProcessor, more at:
	// http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/Bean.html
	public static CustomScopeConfigurer customScopeConfigurer() {
		final CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.addScope("view", new ViewScope());
		return configurer;
	}

}
