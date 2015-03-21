package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import sk.tuke.kpi.eprez.streamer.SharedData;
import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;

import com.allanbank.mongodb.bson.element.ObjectId;

public class PlaybackRequestHandler implements Handler<HttpServerRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackRequestHandler.class);

	protected final Vertx vertx;

	private final Map<String, AbstractMulticastPump> multicastBus;

	public PlaybackRequestHandler(final Vertx vertx, final Map<String, AbstractMulticastPump> multicastBus) {
		this.vertx = vertx;
		this.multicastBus = multicastBus;
	}

	@Override
	public void handle(final HttpServerRequest req) {
		final String sessionToken = req.params().get("token");
		SharedData.presentation().findBySessionToken(sessionToken, (throwable, result) -> {
			final String token = ((ObjectId) result.get("_id").getValueAsObject()).toHexString();
			// TODO validate token
			// LOGGER.error("Multicast on address token " + token + " is not available");
			// req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();

			LOGGER.info("Received new listener on playback uri with sessionToken: " + token);
			final HttpServerResponse response = req.response();
			multicastBus.get(token).add(response.setChunked(true), sessionToken);
			response.closeHandler(event -> {
				LOGGER.info("Connection with listener has been lost...removing from multicast bus");
				multicastBus.get(token).remove(response);
			});
		});
	}
}
