package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.helpers.Formatter;
import sk.tuke.kpi.eprez.streamer.pumps.AbstractMulticastPump;

public class PresenterWebSocketHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresenterWebSocketHandler.class);

	private static final String RECORDING_FILES = "C:\\Users\\pchov_000\\Desktop\\";

	protected final Vertx vertx;

	private final Map<String, AbstractMulticastPump> multicastBus;

	public PresenterWebSocketHandler(final Vertx vertx, final Map<String, AbstractMulticastPump> multicastBus) {
		this.vertx = vertx;
		this.multicastBus = multicastBus;
	}

	@Override
	public void handle(final ServerWebSocket socket) {
		final String token = socket.headers().get("token");
		if (token == null) { // TODO validate token
			LOGGER.info("Invalid recording uri: " + socket.uri());
			socket.close();
		} else {
			LOGGER.info("Recording uri is valid: token=" + token);

			final WebSocketMessageHandler socketMessageHandler = new WebSocketMessageHandler(socket);
			final AbstractMulticastPump multicastStreamPump = socketMessageHandler.pump("audio.stream.publish").start();
			multicastBus.put(token, multicastStreamPump);

			final long timerId = vertx.setPeriodic(3000, event -> {
				final String bytesFormatted = Formatter.bytes(multicastStreamPump.bytesPumped());
				LOGGER.info("MulticastStreamPump report: listeners = " + multicastStreamPump.getListenersCount() + ", total pumped bytes = " + bytesFormatted);
			});

			socket.closeHandler(socketCloseEvent -> {
				LOGGER.info("Closing socket");
				multicastStreamPump.stop();
				vertx.cancelTimer(timerId);
				multicastBus.remove(token);
			});

			// Recording audio stream also to file
			final String fileName = RECORDING_FILES + token + ".mp3";
			vertx.fileSystem().delete(fileName, event -> {
				vertx.fileSystem().open(fileName, recordingFile -> {
					if (recordingFile.succeeded()) {
						multicastStreamPump.add(recordingFile.result());
					} else {
						LOGGER.error("Recording failed: cannot open file for recording");
					}
				});
			});

		}
	}
}
