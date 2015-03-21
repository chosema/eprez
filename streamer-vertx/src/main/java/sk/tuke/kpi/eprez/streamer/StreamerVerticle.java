package sk.tuke.kpi.eprez.streamer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import sk.tuke.kpi.eprez.streamer.handlers.ListenerWebSocketHandler;
import sk.tuke.kpi.eprez.streamer.handlers.PlaybackRequestHandler;
import sk.tuke.kpi.eprez.streamer.handlers.PresenterWebSocketHandler;
import sk.tuke.kpi.eprez.streamer.handlers.WebSocketHandlerDispatcher;
import sk.tuke.kpi.eprez.streamer.helpers.JsonHelper;
import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;
import sk.tuke.kpi.eprez.streamer.pumps.MulticastHandlerPump;

public class StreamerVerticle extends Verticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerticleExecutor.class);

	private final int port;

	private final Map<String, AbstractMulticastPump> multicastBus = new HashMap<String, AbstractMulticastPump>() {
		private static final long serialVersionUID = 1L;

		@Override
		public AbstractMulticastPump get(final Object key) {
			if (!containsKey(key)) {
				multicastBus.put(String.valueOf(key), MulticastHandlerPump.createPump("audio.stream.publish"));
			}
			return super.get(key);
		}
	};

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
			.get("/play/:token", new PlaybackRequestHandler(vertx, multicastBus))
			.get("/info/:token", req -> SharedData.presentation().findBySessionToken(req.params().get("token"), JsonHelper.documentWriter(req.response()))));
		//@formatter:on

		//@formatter:off
		httpServer.websocketHandler(new WebSocketHandlerDispatcher()
			.on("/listen/:token", new ListenerWebSocketHandler(vertx, multicastBus))
			.on("/record/:token", new PresenterWebSocketHandler(vertx, multicastBus)));
		//@formatter:on

		httpServer.listen(port);
	}

}
