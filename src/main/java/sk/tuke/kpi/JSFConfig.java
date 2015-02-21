package sk.tuke.kpi;

import javax.annotation.Resource;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.sun.faces.config.ConfigureListener;

@Configuration
public class JSFConfig implements ServletContextInitializer {

	@Resource
	private Environment env;

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		// JSF CONFIGURATION PARAMETERS
		if (env.acceptsProfiles(Application.PROFILE_DEV, Application.PROFILE_TEST)) {
			servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
			servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
		} else if (env.acceptsProfiles(Application.PROFILE_PROD)) {
			servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
			servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "0");
		}
		servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
		servletContext.setInitParameter("javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL", "true");
		// State saving method: 'client' or 'server' (=default). See JSF Specification 2.5.2
		servletContext.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
		// Time zone parameter for correct formatting of dates
		servletContext.setInitParameter("javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE", "true");

		// PRIMEFACES CONFIGURATION PARAMETERS
		// Transformation of validation rules (Java Bean Validation) to forms, ie. NotNull, MaxLength,...
		servletContext.setInitParameter("primefaces.TRANSFORM_METADATA", "true");
		// Use Apache commons-fileupload in File Upload component
		servletContext.setInitParameter("primefaces.UPLOADER", "commons");
		// PrimeFaces Theme (use none for no theme)
		servletContext.setInitParameter("primefaces.THEME", "bootstrap");
	}

	@Bean
	public ServletRegistrationBean facesServletRegistration() {
		return new ServletRegistrationBean(new FacesServlet(), "*.xhtml");
	}

	@Bean
	public ServletListenerRegistrationBean<ConfigureListener> configureListenerRegistration() {
		return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
	}

}
