package sk.tuke.kpi;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sk.tuke.kpi.web.ViewScope;

@Configuration
public class ScopesConfig {

	public @Bean CustomScopeConfigurer customScopeConfigurer() {
		final CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.addScope("view", new ViewScope());
		return configurer;
	}

}
