package sk.tuke.kpi.eprez;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(DataConfig.class);

	@Value("${data.mongodb.host}")
	String host;
	@Value("${data.mongodb.port}")
	Integer port;
	@Value("${data.mongodb.database}")
	String database;

	@Value("${data.mongodb.username}")
	String username;
	@Value("${data.mongodb.password}")
	String password;

//	@Bean
//	@Profile(CoreConfig.PROFILE_DEV)
//	public ServletRegistrationBean h2servletRegistration() {
//		final ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
//		registration.addUrlMappings("/console/*");
//		return registration;
//	}

	// Factory bean that creates the com.mongodb.Mongo instance
	public @Bean MongoFactoryBean mongo() {
		final MongoFactoryBean mongo = new MongoFactoryBean();
		mongo.setHost(host);
		mongo.setPort(port);
		return mongo;
	}

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		final String connectMessage = "Connecting to MongoDB on " + host + ":" + port;
		if (StringUtils.isEmpty(username)) {
			LOGGER.info(connectMessage + " to database " + database);
			return new SimpleMongoDbFactory(mongo().getObject(), database);
		} else {
			LOGGER.info(connectMessage + " to database " + database + " with username " + username);
			return new SimpleMongoDbFactory(mongo().getObject(), database, new UserCredentials(username, password));
		}
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		final MongoTemplate template = new MongoTemplate(mongoDbFactory());
		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		return template;
	}
}
