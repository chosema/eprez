package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.http.ServerWebSocket;

import sk.tuke.kpi.eprez.streamer.helpers.AbstractMulticastPump;
import sk.tuke.kpi.eprez.streamer.helpers.MulticastStreamPump;

public class RecordHandler implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordHandler.class);

	private static final String RECORDING_FILES = "C:\\Users\\pchov_000\\Desktop\\";

	protected final Vertx vertx;

	private final Map<String, AbstractMulticastPump> multicastBus;

	public RecordHandler(final Vertx vertx, final Map<String, AbstractMulticastPump> multicastBus) {
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

					//final WebSocketMessageHandler socketMessageHandler = new WebSocketMessageHandler(socket);

					final AbstractMulticastPump multicastStreamPump = MulticastStreamPump.createPump(socket).add(file).start();
					multicastBus.put(token, multicastStreamPump);

					final long timerId = vertx.setPeriodic(1000, event -> {
						final String bytesPumped = FileUtils.byteCountToDisplaySize(multicastStreamPump.bytesPumped());
						LOGGER.info("MulticastStreamPump report: listeners = " + multicastStreamPump.getWriteStreamsCount() + ", total pumped bytes = " + bytesPumped);
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
