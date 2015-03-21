package sk.tuke.kpi.eprez.streamer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.allanbank.mongodb.Credential;
import com.allanbank.mongodb.LambdaCallback;
import com.allanbank.mongodb.MongoClient;
import com.allanbank.mongodb.MongoClientConfiguration;
import com.allanbank.mongodb.MongoCollection;
import com.allanbank.mongodb.MongoDatabase;
import com.allanbank.mongodb.MongoFactory;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.bson.element.ObjectId;
import com.allanbank.mongodb.builder.QueryBuilder;

public final class SharedData {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharedData.class);

	private static Properties properties;
	private static MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;

	static {
		InputStream applicationPropertiesStream = null;
		try {
			applicationPropertiesStream = VerticleExecutor.class.getResourceAsStream("/application.properties");

			properties = new Properties();
			properties.load(applicationPropertiesStream);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(applicationPropertiesStream);
		}
	}

	static {
		final String host = properties.getProperty("data.mongodb.host");
		final String port = properties.getProperty("data.mongodb.port");
		final String database = properties.getProperty("data.mongodb.database");
		final String username = properties.getProperty("data.mongodb.username");
		final String password = properties.getProperty("data.mongodb.password");

		final MongoClientConfiguration config = new MongoClientConfiguration();
		config.addServer(host + ":" + port);
		if (StringUtils.isNotEmpty(username)) {
			config.addCredential(Credential.builder().userName(username).password(password.toCharArray()).database(database).mongodbCR());
		}

		mongoClient = MongoFactory.createClient(config);
		mongoDatabase = mongoClient.getDatabase(database);
	}

	public interface Presentation {
		void findBySessionToken(String token, LambdaCallback<Document> callback);
	}

	private static Presentation presentation = new Presentation() {
		@Override
		public void findBySessionToken(final String token, final LambdaCallback<Document> callback) {
			try {
				getCollectionPresentation().findOneAsync(callback, QueryBuilder.where("session.tokens.$id").equals(new ObjectId(token)));
			} catch (final Exception e) {
				LOGGER.error("Error occured on call to MongoDb", e);
				callback.accept(e, null);
			}
		}
	};

	private SharedData() { // default constructor
	}

	public static Properties getProperties() {
		return properties;
	}

	public static MongoClient getMongoClient() {
		return mongoClient;
	}

	public static MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}

	private static MongoCollection getCollectionPresentation() {
		return mongoDatabase.getCollection("presentation");
	}

	public static Presentation presentation() {
		return presentation;
	}
}
