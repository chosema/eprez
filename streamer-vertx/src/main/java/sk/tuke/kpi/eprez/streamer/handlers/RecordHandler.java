package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.helpers.MulticastPump;

public class RecordHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordHandler.class);

	private static final String RECORDING_FILES = "C:\\Users\\pchov_000\\Desktop\\";

	protected final Vertx vertx;

	private final Map<String, MulticastPump> multicastBus;

	public RecordHandler(final Vertx vertx, final Map<String, MulticastPump> multicastBus) {
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

			final String fileName = token + ".mp3";
			vertx.fileSystem().open(RECORDING_FILES + fileName, recordingFile -> {
				if (recordingFile.succeeded()) {
					final AsyncFile file = recordingFile.result();

					final MulticastPump multicastPump = MulticastPump.createPump(socket, file).start();
					multicastBus.put(token, multicastPump);

					final long timerId = vertx.setPeriodic(1000, event -> {
						LOGGER.info("MulticastPump report: listeners = " + multicastPump.getWriteStreamsCount() + ", total pumped bytes = " + multicastPump.bytesPumped());
					});

					socket.closeHandler(socketCloseEvent -> {
						LOGGER.info("Closing socket");
						vertx.cancelTimer(timerId);
						multicastBus.remove(token);
						file.close();
					});
				} else {
					LOGGER.error("Recording failed: cannot open file for recording");
				}
			});
		}
	}

}
