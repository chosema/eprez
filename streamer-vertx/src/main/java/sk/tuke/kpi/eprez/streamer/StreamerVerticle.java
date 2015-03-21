package sk.tuke.kpi.eprez.streamer;

import io.netty.handler.codec.http.HttpResponseStatus;

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

public class StreamerVerticle extends Verticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerticleExecutor.class);

	private final int port;

	public StreamerVerticle(final int port) {
		this.port = port;
	}

	@Override
	public void start() {
		LOGGER.info("Creating WebSocket server backed up by HTTP server on port=" + port);

		final HttpServer httpServer = vertx.createHttpServer();

		//@formatter:off
		httpServer.requestHandler(new RouteMatcher()
			.noMatch(req -> {
				LOGGER.error("Received HTTP request with no routing: " + req.path());
				req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
			})
			.get("/play/:token", new PlaybackRequestHandler(vertx))
			.get("/info/:token.json", req -> SharedData.presentation().findBySessionToken(req.params().get("token"), JsonHelper.documentWriter(req.response())))
			.get("/data/:id", req -> SharedData.getData().findOne(req.params().get("id"), JsonHelper.dataWriter(req.response()))));
		//@formatter:on

		//@formatter:off
		httpServer.websocketHandler(new WebSocketHandlerDispatcher()
			.on("/listen/:token", new ListenerWebSocketHandler(vertx))
			.on("/record/:token", new PresenterWebSocketHandler(vertx)));
		//@formatter:on

		httpServer.listen(port);
	}

}
