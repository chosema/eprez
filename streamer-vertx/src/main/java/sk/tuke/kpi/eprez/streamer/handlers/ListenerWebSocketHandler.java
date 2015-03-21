package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;

public class ListenerWebSocketHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresenterWebSocketHandler.class);

	private final Vertx vertx;

	private final Map<String, AbstractMulticastPump> multicastBus;

	public ListenerWebSocketHandler(final Vertx vertx, final Map<String, AbstractMulticastPump> multicastBus) {
		this.vertx = vertx;
		this.multicastBus = multicastBus;
	}

	@Override
	public void handle(final ServerWebSocket socket) {
		final String sessionToken = socket.headers().get("token");

		LOGGER.info("Received new listener on listening uri with sessionToken: " + sessionToken);
	}

}
