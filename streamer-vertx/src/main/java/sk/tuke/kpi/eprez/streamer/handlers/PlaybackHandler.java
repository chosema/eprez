package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import sk.tuke.kpi.eprez.streamer.helpers.MulticastPump;

public class PlaybackHandler implements Handler<HttpServerRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackHandler.class);

	protected final Vertx vertx;

	private final Map<String, MulticastPump> multicastBus;

	public PlaybackHandler(final Vertx vertx, final Map<String, MulticastPump> multicastBus) {
		this.vertx = vertx;
		this.multicastBus = multicastBus;
	}

	@Override
	public void handle(final HttpServerRequest req) {
		final String token = req.params().get("token");
		final HttpServerResponse response = req.response();
		if (token == null) {
			LOGGER.error("New listener received on invalid playback uri: " + req.uri());
			response.setStatusCode(HttpStatus.SC_NOT_FOUND);
			response.end();
		} else {
			LOGGER.info("New listener received on playback uri with token=" + token);
			if (multicastBus.containsKey(token)) {
				multicastBus.get(token).add(response.setChunked(true));
				response.closeHandler(event -> {
					LOGGER.info("");
					multicastBus.get(token).remove(response);
				});
			} else {
				LOGGER.error("Multicast on address token " + token + " is not available");
				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				response.end();
			}
		}
	}

}
