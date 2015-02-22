package sk.tuke.kpi.eprez;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
@EnableMongoAuditing(auditorAwareRef = "loggedUserProvider")
public class DataConfig {

//	@Bean
//	@Profile(Application.PROFILE_DEV)
//	public ServletRegistrationBean h2servletRegistration() {
//		final ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
//		registration.addUrlMappings("/console/*");
//		return registration;
//	}

	/*
	* Factory bean that creates the com.mongodb.Mongo instance
	*/
	public @Bean MongoFactoryBean mongo() {
		final MongoFactoryBean mongo = new MongoFactoryBean();
		mongo.setHost("localhost");
		return mongo;
	}

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongo().getObject(), "eprez");
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		final MongoTemplate template = new MongoTemplate(mongoDbFactory());
		template.setWriteResultChecking(WriteResultChecking.LOG);
		return template;
	}
}
