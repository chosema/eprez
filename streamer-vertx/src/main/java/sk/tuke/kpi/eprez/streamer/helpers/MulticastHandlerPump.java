package sk.tuke.kpi.eprez.streamer.helpers;

import sk.tuke.kpi.eprez.streamer.handlers.WebSocketMessageHandler;

public class MulticastHandlerPump extends AbstractMulticastPump {

	private final String message;
	private final WebSocketMessageHandler handler;

	private MulticastHandlerPump(final String message, final WebSocketMessageHandler handler, final int maxWriteQueueSize) {
		super(maxWriteQueueSize);
		this.message = message;
		this.handler = handler;
	}

	private MulticastHandlerPump(final String message, final WebSocketMessageHandler handler) {
		this.message = message;
		this.handler = handler;
	}

	public static MulticastHandlerPump createPump(final String message, final WebSocketMessageHandler socketMessageHandler) {
		return new MulticastHandlerPump(message, socketMessageHandler);
	}

	@Override
	public MulticastHandlerPump start() {
		handler.on(message, dataHandler);
		return this;
	}

	@Override
	public MulticastHandlerPump stop() {
		handler.cancel(message);
		return this;
	}
}
