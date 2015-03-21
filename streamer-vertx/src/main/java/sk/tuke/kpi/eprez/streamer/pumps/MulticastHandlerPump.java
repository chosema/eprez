package sk.tuke.kpi.eprez.streamer.pumps;

import sk.tuke.kpi.eprez.streamer.handlers.WebSocketMessageHandler;

public class MulticastHandlerPump extends AbstractMulticastPump {

	private final String message;
	private WebSocketMessageHandler handler;

	private MulticastHandlerPump(final String message, final WebSocketMessageHandler handler, final int maxWriteQueueSize) {
		super(maxWriteQueueSize);
		this.message = message;
		this.handler = handler;
	}

	private MulticastHandlerPump(final String message, final WebSocketMessageHandler handler) {
		this.message = message;
		this.handler = handler;
	}

	private MulticastHandlerPump(final String message) {
		this.message = message;
	}

	public static MulticastHandlerPump createPump(final String message) {
		return new MulticastHandlerPump(message);
	}

	public static MulticastHandlerPump createPump(final String message, final WebSocketMessageHandler socketMessageHandler) {
		return new MulticastHandlerPump(message, socketMessageHandler);
	}

	public MulticastHandlerPump handler(final WebSocketMessageHandler handler) {
		this.handler = handler;
		return this;
	}

	@Override
	public MulticastHandlerPump start() {
		if (handler != null) {
			handler.on(message, dataHandler);
		}
		return this;
	}

	@Override
	public MulticastHandlerPump stop() {
		if (handler != null) {
			handler.cancel(message);
		}
		return this;
	}
}
