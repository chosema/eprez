package sk.tuke.kpi.eprez.streamer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;

import sk.tuke.kpi.eprez.streamer.EventBusAddressHolder;
import sk.tuke.kpi.eprez.streamer.SharedData;
import sk.tuke.kpi.eprez.streamer.helpers.ChatMessagesHelper;
import sk.tuke.kpi.eprez.streamer.helpers.ConnectedListenersHelper;

import com.allanbank.mongodb.bson.element.ObjectId;

public class PresenterWebSocketHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresenterWebSocketHandler.class);

	protected static final String RECORDING_FILES = "C:\\Users\\pchov_000\\Desktop\\";

	protected final Vertx vertx;

	public PresenterWebSocketHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(final ServerWebSocket socket) {
		final String sessionToken = socket.headers().get("token");

		SharedData.presentation().findBySessionToken(sessionToken, (throwable, result) -> {
			LOGGER.info("Received new recorder on recording uri with sessionToken: " + sessionToken);

			final String presentationId = ((ObjectId) result.get("_id").getValueAsObject()).toHexString();
			final String audioStreamAddress = EventBusAddressHolder.presentationAudioStream(presentationId);

			final WebSocketMessageHandler socketMessageHandler = new WebSocketMessageHandler(socket);
			socketMessageHandler.on("send:audio.stream.publish", buffer -> {
				vertx.eventBus().publish(audioStreamAddress, buffer);
			});
			socketMessageHandler.on("send:document.stream.publish", buffer -> {
				LOGGER.info("New document published: " + buffer);
				final String encodedMessage = new JsonObject().putString("type", "push:document.stream.publish").putString("content", buffer.toString()).encode();
				vertx.setTimer(5000, event -> vertx.eventBus().publish(EventBusAddressHolder.presentationDocumentStream(presentationId), encodedMessage));
				socket.writeTextFrame(encodedMessage);
			});

			/* add listeners helper */
			final ConnectedListenersHelper connectedListenersHelper = new ConnectedListenersHelper(vertx, socket).addListener(presentationId, sessionToken);
			/* add chat messages helper */
			final ChatMessagesHelper chatMessagesHelper = new ChatMessagesHelper(vertx, socketMessageHandler).addListener(presentationId, sessionToken);

			socket.closeHandler(socketCloseEvent -> {
				LOGGER.info("Closing socket");
				connectedListenersHelper.removeListener(presentationId, sessionToken);
				chatMessagesHelper.removeListener(presentationId);
			});

//			final String fileName = RECORDING_FILES + token + ".mp3";
//			vertx.fileSystem().delete(fileName, event -> {
//				vertx.fileSystem().open(fileName, recordingFile -> {
//					if (recordingFile.succeeded()) {
//						multicastStreamPump.add(recordingFile.result());
//					} else {
//						LOGGER.error("Recording failed: cannot open file for recording");
//					}
//				});
//			});
			});
	}
}
