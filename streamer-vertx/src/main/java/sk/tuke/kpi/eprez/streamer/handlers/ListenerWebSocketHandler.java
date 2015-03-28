package sk.tuke.kpi.eprez.streamer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.EventBusAddressHolder;
import sk.tuke.kpi.eprez.streamer.SharedData;
import sk.tuke.kpi.eprez.streamer.helpers.ConnectedListenersHelper;

import com.allanbank.mongodb.bson.element.ObjectId;

public class ListenerWebSocketHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresenterWebSocketHandler.class);

	protected final Vertx vertx;

	public ListenerWebSocketHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(final ServerWebSocket socket) {
		final String sessionToken = socket.headers().get("token");

		SharedData.presentation().findBySessionToken(sessionToken, (throwable, result) -> {
			final String presentationId = ((ObjectId) result.get("_id").getValueAsObject()).toHexString();

			/* add connected listener */
			final ConnectedListenersHelper connectedListenersHelper = new ConnectedListenersHelper(vertx, socket).addListener(presentationId, sessionToken);

			/* presentation document handler */
			final String presentationDocumentStreamAddress = EventBusAddressHolder.presentationDocumentStream(presentationId);
			final Handler<Message<?>> presentationDocumentStreamHandler = (final Message<?> message) -> {
				socket.writeTextFrame(message.body().toString());
			};
			vertx.eventBus().registerHandler(presentationDocumentStreamAddress, presentationDocumentStreamHandler);

			/* Socket end */
			socket.closeHandler(event -> {
				vertx.eventBus().unregisterHandler(presentationDocumentStreamAddress, presentationDocumentStreamHandler);
				connectedListenersHelper.removeListener(presentationId, sessionToken);
			});
			socket.exceptionHandler(e -> {
				LOGGER.error(e.getMessage(), e);
				vertx.eventBus().unregisterHandler(presentationDocumentStreamAddress, presentationDocumentStreamHandler);
				connectedListenersHelper.removeListener(presentationId, sessionToken);
			});
		});

		LOGGER.info("Received new listener on listening uri with sessionToken: " + sessionToken);
	}
}
