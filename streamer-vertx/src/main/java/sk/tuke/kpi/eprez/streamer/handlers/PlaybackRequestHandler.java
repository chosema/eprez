package sk.tuke.kpi.eprez.streamer.handlers;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;

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
		final String token = req.params().get("token");
		final HttpServerResponse response = req.response();
		if (multicastBus.containsKey(token)) {
			LOGGER.info("New listener received on playback uri with token: " + token);
			multicastBus.get(token).add(response.setChunked(true), token);
			response.closeHandler(event -> {
				LOGGER.info("Connection with listener has been lost...removing from multicast bus");
				multicastBus.get(token).remove(response);
			});
		} else {
			LOGGER.error("Multicast on address token " + token + " is not available");
			response.setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
		}
	}
}
