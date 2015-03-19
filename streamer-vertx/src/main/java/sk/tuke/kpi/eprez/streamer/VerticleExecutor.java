package sk.tuke.kpi.eprez.streamer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;

public class VerticleExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerticleExecutor.class);

	public static void main(final String[] args) throws IOException {

		final Vertx vertx = VertxFactory.newVertx();

		LOGGER.info("Creating streamer verticle");
		final StreamerVerticle verticle = new StreamerVerticle(8090);
		verticle.setVertx(vertx);
		verticle.start();

		System.in.read(); // Prevent the JVM from exiting
	}
}
