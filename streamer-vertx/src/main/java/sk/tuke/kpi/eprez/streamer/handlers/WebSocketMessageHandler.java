package sk.tuke.kpi.eprez.streamer.handlers;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.ServerWebSocket;

public class WebSocketMessageHandler implements Handler<Buffer> {

	private final ServerWebSocket socket;

	public WebSocketMessageHandler(final ServerWebSocket socket) {
		this.socket = socket;
		this.socket.dataHandler(this);
	}

	@Override
	public void handle(final Buffer buffer) {
		final byte[] bytes = buffer.getBytes();
		// TODO separate bytes
	}

	public WebSocketMessageHandler on(final String message, final Handler<Buffer> handler) {
		// TODO Auto-generated method stub
		return this;
	}

	public WebSocketMessageHandler cancel(final String message) {
		// TODO Auto-generated method stub
		return this;
	}
}
