package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import sk.tuke.kpi.eprez.streamer.EventBusAddressHolder;
import sk.tuke.kpi.eprez.streamer.SharedData;
import sk.tuke.kpi.eprez.streamer.helpers.Formatter;

import com.allanbank.mongodb.bson.element.ObjectId;

public class PlaybackRequestHandler implements Handler<HttpServerRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackRequestHandler.class);

	private static final int MAX_QUEUE_SIZE = 64 * 1024; // 64 kB

	protected final Vertx vertx;

	private final Map<String, Long[]> listenersCount = new HashMap<>();

	public PlaybackRequestHandler(final Vertx vertx) {
		this.vertx = vertx;
		vertx.setPeriodic(5000, event -> {
			LOGGER.info("Multicast report:" + format(listenersCount));
		});

	}

	@Override
	public void handle(final HttpServerRequest req) {
		final String sessionToken = req.params().get("token");
		// TODO validate sessionToken and presentationId
		// LOGGER.error("Multicast on address " + presentationId + " is not available");
		// req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();

		SharedData.presentation().findBySessionToken(sessionToken, (throwable, result) -> {
			final String presentationId = ((ObjectId) result.get("_id").getValueAsObject()).toHexString();

			final String audioStreamAddress = EventBusAddressHolder.presentationAudioStream(presentationId);

			final HttpServerResponse response = req.response().setChunked(true);
			response.setWriteQueueMaxSize(MAX_QUEUE_SIZE);
			final Handler<Message<Buffer>> audioStreamHandler = (final Message<Buffer> message) -> {
				if (response.writeQueueFull()) {
					LOGGER.warn("Listener [" + sessionToken + "] has reached full writing queue...can not write any more data");
				} else {
					response.write(message.body());
					increment(audioStreamAddress, 1, message.body().length());
				}
			};

//			LOGGER.info("Received new listener with token: " + sessionToken);
//			LOGGER.info("Registering him on presentation eventBus stream address: " + audioStreamAddress);

			vertx.eventBus().registerHandler(audioStreamAddress, audioStreamHandler);
			increment(audioStreamAddress, 0, 1);
			response.closeHandler(event -> {
//				LOGGER.info("Connection with listener has been lost...removing from eventBus address: " + audioStreamAddress);
				decrement(audioStreamAddress, 0, 1);
				vertx.eventBus().unregisterHandler(audioStreamAddress, audioStreamHandler);
			});
		});
	}

	public String format(final Map<String, Long[]> map) {
		final StringBuilder sb = new StringBuilder();
		for (final Entry<String, Long[]> entry : map.entrySet()) {
			sb.append("\n\taddress=").append(entry.getKey());
			sb.append(", listeners=").append(entry.getValue()[0]);
			sb.append(", total distributed data=" + Formatter.bytes(entry.getValue()[1]));
		}
		return sb.toString();
	}

	public void increment(final String key, final int index, final long value) {
		Long[] data = listenersCount.get(key);
		if (data == null) {
			listenersCount.put(key, data = new Long[] { 0L, 0L });
		}
		data[index] += value;
	}

	public void decrement(final String key, final int index, final long value) {
		Long[] data = listenersCount.get(key);
		if (data == null) {
			listenersCount.put(key, data = new Long[] { 0L, 0L });
		}
		data[index] -= value;
	}
}
