package sk.tuke.kpi.eprez.streamer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import sk.tuke.kpi.eprez.streamer.handlers.PlaybackHandler;
import sk.tuke.kpi.eprez.streamer.handlers.RecordHandler;
import sk.tuke.kpi.eprez.streamer.handlers.WebSocketHandlerDispatcher;
import sk.tuke.kpi.eprez.streamer.helpers.MulticastPump;

public class StreamerVerticle extends Verticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerticleExecutor.class);

	private final int port;

	private final Map<String, MulticastPump> multicastBus = new HashMap<String, MulticastPump>();

	public StreamerVerticle(final int port) {
		this.port = port;
	}

	@Override
	public void start() {
		LOGGER.info("Creating WebSocket server backed up by HTTP server on port=" + port);

		final HttpServer httpServer = vertx.createHttpServer();

		// Playback handler
		//@formatter:off
		final RouteMatcher httpRouteMatcher = new RouteMatcher()
			.noMatch(req -> LOGGER.error("Received HTTP request with no routing: " + req.path()))
			.get("/index.html", req -> req.response().sendFile("C:\\Users\\pchov_000\\Desktop\\index.html"))
			.get("/play/:token", new PlaybackHandler(vertx, multicastBus));
		httpServer.requestHandler(httpRouteMatcher);
		//@formatter:on

		// WS recording handler
		//@formatter:off
		final WebSocketHandlerDispatcher webSocketDispatcher = new WebSocketHandlerDispatcher()
			.on("/record/:token", new RecordHandler(vertx, multicastBus));
		//@formatter:on
		httpServer.websocketHandler(webSocketDispatcher);

		httpServer.listen(port);
	}
}
