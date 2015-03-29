package sk.tuke.kpi.eprez.streamer.handlers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.pumps.MulticastHandlerPump;

public class WebSocketMessageHandler implements Handler<Buffer> {

	private static final byte BYTE_SEPARATOR = 0;
	private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

	private ServerWebSocket socket;

	private final Map<String, List<Handler<Buffer>>> handlers = new HashMap<>();
	private Handler<Buffer> defaultHandler;

	public WebSocketMessageHandler() { // do nothing
	}

	public WebSocketMessageHandler(final ServerWebSocket socket) {
		this.socket = socket;
		socket.dataHandler(this);
	}

	public ServerWebSocket getSocket() {
		return socket;
	}

	@Override
	public void handle(final Buffer buffer) {
		final byte[] bytes = buffer.getBytes();
		final int indexOfByteSeparator = ArrayUtils.indexOf(bytes, BYTE_SEPARATOR);
		if (indexOfByteSeparator != ArrayUtils.INDEX_NOT_FOUND) {
			final String message = indexOfByteSeparator == 0 ? null : new String(ArrayUtils.subarray(bytes, 0, indexOfByteSeparator), CHARSET_UTF_8);
			final Buffer data = buffer.getBuffer(indexOfByteSeparator + 1, buffer.length());

			final List<Handler<Buffer>> messageHandlers = handlers.get(message);
			if (messageHandlers == null) {
				if (defaultHandler != null) {
					defaultHandler.handle(data);
				}
			} else {
				for (final Handler<Buffer> handler : messageHandlers) {
					handler.handle(data);
				}
			}
		}
	}

	public WebSocketMessageHandler on(final String message, final Handler<Buffer> handler) {
		List<Handler<Buffer>> messageHandlers = handlers.get(message);
		if (messageHandlers == null) {
			messageHandlers = new ArrayList<Handler<Buffer>>(2);
		}
		messageHandlers.add(handler);
		handlers.put(message, messageHandlers);
		return this;
	}

	public WebSocketMessageHandler defaultHandler(final Handler<Buffer> handler) {
		this.defaultHandler = handler;
		return this;
	}

	public WebSocketMessageHandler cancel(final String message) {
		handlers.remove(message);
		return this;
	}

	public MulticastHandlerPump pump(final String message) {
		return MulticastHandlerPump.createPump(message, this);
	}

	public MulticastHandlerPump pump(final MulticastHandlerPump pump) {
		return pump;
	}
}
