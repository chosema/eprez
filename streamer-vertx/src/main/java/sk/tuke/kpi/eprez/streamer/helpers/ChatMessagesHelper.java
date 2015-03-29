package sk.tuke.kpi.eprez.streamer.helpers;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import sk.tuke.kpi.eprez.streamer.handlers.WebSocketMessageHandler;

public class ChatMessagesHelper {

	private final Vertx vertx;
	private final WebSocketMessageHandler socket;

	private String presentationChatRoomAddress;
	private Handler<? extends Message<JsonObject>> messageHandler;

	public ChatMessagesHelper(final Vertx vertx, final WebSocketMessageHandler socket) {
		this.vertx = vertx;
		this.socket = socket;
	}

	public ChatMessagesHelper addListener(final String presentationId, final String listenerToken) {
		presentationChatRoomAddress = "presentation." + presentationId + ".chatRoom";

		// receive message
		socket.on("send:presentation.chat.message", buffer -> {
			final JsonObject message = new JsonObject();
			message.putString("type", "push:presentation.chat.message");
			message.putObject("content", new JsonObject().putString("token", listenerToken).putString("message", buffer.toString()));
			vertx.eventBus().publish(presentationChatRoomAddress, message); /* publish chat message */
		});

		vertx.eventBus().registerHandler(presentationChatRoomAddress, (messageHandler = message -> {
			socket.getSocket().writeTextFrame(message.body().encode());
		}));
		return this;
	}

	public ChatMessagesHelper removeListener(final String presentationId) {
		vertx.eventBus().unregisterHandler(presentationChatRoomAddress, messageHandler);
		return this;
	}
}
