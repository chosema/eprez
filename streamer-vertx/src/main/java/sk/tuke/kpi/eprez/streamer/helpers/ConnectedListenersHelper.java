package sk.tuke.kpi.eprez.streamer.helpers;

import java.util.ArrayList;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class ConnectedListenersHelper {

	private final Vertx vertx;
	private final ServerWebSocket socket;

	private Handler<? extends Message<?>> connectedHandler;
	private Handler<? extends Message<?>> disconnectedHandler;

	public ConnectedListenersHelper(final Vertx vertx, final ServerWebSocket socket) {
		this.vertx = vertx;
		this.socket = socket;
	}

	public ConnectedListenersHelper addListener(final String presentationId, final String listenerToken) {
		final Set<String> listeners = vertx.sharedData().getSet("presentation." + presentationId + ".listeners");
		listeners.add(listenerToken);

		final String connectAddress = "presentation." + presentationId + ".listener.connect";
		final String disconnectAddress = "presentation." + presentationId + ".listener.disconnect";

		connectedHandler = event -> {
			final Set<String> currentListeners = vertx.sharedData().getSet("presentation." + presentationId + ".listeners");
			final String encodedMessage = new JsonObject().putString("type", "push:presentation.listeners")
					.putValue("content", new JsonArray(new ArrayList<String>(currentListeners))).encode();
			socket.writeTextFrame(encodedMessage);
		};

		disconnectedHandler = message -> {
			final Set<String> currentListeners = vertx.sharedData().getSet("presentation." + presentationId + ".listeners");
			final String encodedMessage = new JsonObject().putString("type", "push:presentation.listeners")
					.putValue("content", new JsonArray(new ArrayList<String>(currentListeners))).encode();
			if (listenerToken.equals(message.body().toString())) {
				vertx.eventBus().unregisterHandler(connectAddress, connectedHandler);
				vertx.eventBus().unregisterHandler(disconnectAddress, disconnectedHandler);
			} else {
				socket.writeTextFrame(encodedMessage);
			}
		};

		vertx.eventBus().registerHandler(connectAddress, connectedHandler);
		vertx.eventBus().registerHandler(disconnectAddress, disconnectedHandler);
		vertx.eventBus().publish(connectAddress, listenerToken);

		return this;
	}

	public ConnectedListenersHelper removeListener(final String presentationId, final String listenerToken) {
		final Set<String> listeners = vertx.sharedData().getSet("presentation." + presentationId + ".listeners");
		if (listeners.contains(listenerToken)) {
			listeners.remove(listenerToken);
			vertx.eventBus().publish("presentation." + presentationId + ".listener.disconnect", listenerToken);
		}
		return this;
	}
}
