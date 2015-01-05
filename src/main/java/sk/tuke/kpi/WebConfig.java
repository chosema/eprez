package sk.tuke.kpi;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class WebConfig {

	@Resource
	private Environment env;

	@Resource
	private MessageSource messageSource;

	@Bean
	public TemplateResolver templateResolver() {
		final TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setCharacterEncoding(Application.ENCODING);
		// XHTML is the default mode, but we set it anyway for better understanding of code
		templateResolver.setTemplateMode("HTML5");
		// This will convert "home" to "/WEB-INF/templates/home.html"
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		// Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
		if (env.acceptsProfiles(Application.PROFILE_DEV)) {
			templateResolver.setCacheable(false);
		} else {
			templateResolver.setCacheable(true);
			templateResolver.setCacheTTLMs(3600000L);
		}
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ViewResolver viewResolver() {
		final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		viewResolver.setViewNames(new String[] { "*.html" });
		return viewResolver;
	}

}
