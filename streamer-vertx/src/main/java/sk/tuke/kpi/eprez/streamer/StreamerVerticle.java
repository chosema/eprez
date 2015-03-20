package sk.tuke.kpi.eprez.streamer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import sk.tuke.kpi.eprez.streamer.handlers.PlaybackRequestHandler;
import sk.tuke.kpi.eprez.streamer.handlers.PresenterWebSocketHandler;
import sk.tuke.kpi.eprez.streamer.handlers.WebSocketHandlerDispatcher;
import sk.tuke.kpi.eprez.streamer.helpers.JsonHelper;
import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;

public class StreamerVerticle extends Verticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerticleExecutor.class);

	private final int port;

	private final Map<String, AbstractMulticastPump> multicastBus = new HashMap<String, AbstractMulticastPump>();

	public StreamerVerticle(final int port) {
		this.port = port;
	}

	@Override
	public void start() {
		LOGGER.info("Creating WebSocket server backed up by HTTP server on port=" + port);

		final HttpServer httpServer = vertx.createHttpServer();

		//@formatter:off
		httpServer.requestHandler(new RouteMatcher()
			.noMatch(req -> LOGGER.error("Received HTTP request with no routing: " + req.path()))
			.get("/index.html", req -> req.response().sendFile("C:\\Users\\pchov_000\\Desktop\\index.html"))
			.get("/play/:token", new PlaybackRequestHandler(vertx, multicastBus))
			.get("/info/:token", req -> SharedData.presentation().findBySessionToken(req.params().get("token"), JsonHelper.documentsWriter(req.response()))));
		//@formatter:on

		//@formatter:off
		httpServer.websocketHandler(new WebSocketHandlerDispatcher()
			.on("/record/:token", new PresenterWebSocketHandler(vertx, multicastBus)));
		//@formatter:on

		httpServer.listen(port);
	}

}
