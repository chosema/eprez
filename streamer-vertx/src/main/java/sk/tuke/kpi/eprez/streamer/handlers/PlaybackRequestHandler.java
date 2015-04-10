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

import com.allanbank.mongodb.bson.element.ObjectId;

public class PlaybackRequestHandler implements Handler<HttpServerRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaybackRequestHandler.class);

	private static final int MAX_QUEUE_SIZE = 64 * 1024; // 64 kB

	protected final Vertx vertx;

	public PlaybackRequestHandler(final Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void handle(final HttpServerRequest req) {
		final String sessionToken = req.params().get("token");
		// TODO validate sessionToken and presentationId
		// LOGGER.error("Multicast on address " + presentationId + " is not available");
		// req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();

		final Map<String, Long> listenersCount = new HashMap<>();

		final long multicastReportTimer = vertx.setPeriodic(5000, event -> {
			LOGGER.info("Multicast report:" + format(listenersCount));
		});

		SharedData.presentation().findBySessionToken(sessionToken, (throwable, result) -> {
			final String presentationId = ((ObjectId) result.get("_id").getValueAsObject()).toHexString();

			final HttpServerResponse response = req.response().setChunked(true);
			response.setWriteQueueMaxSize(MAX_QUEUE_SIZE);
			final Handler<Message<Buffer>> audioStreamHandler = (final Message<Buffer> message) -> {
				if (response.writeQueueFull()) {
					LOGGER.warn("Listener [" + sessionToken + "] has reached full writing queue...can not write any more data");
				} else {
					response.write(message.body());
				}
			};

			final String audioStreamAddress = EventBusAddressHolder.presentationAudioStream(presentationId);

			LOGGER.info("Registering new listener on eventBus address: " + audioStreamAddress);
			vertx.eventBus().registerHandler(audioStreamAddress, audioStreamHandler);
			increment(listenersCount, audioStreamAddress);
			response.closeHandler(event -> {
				LOGGER.info("Connection with listener has been lost...removing from eventBus address: " + audioStreamAddress);
				decrement(listenersCount, audioStreamAddress);
				vertx.eventBus().unregisterHandler(audioStreamAddress, audioStreamHandler);
				vertx.cancelTimer(multicastReportTimer);
			});
		});
	}

	public String format(final Map<String, Long> map) {
		final StringBuilder sb = new StringBuilder();
		for (final Entry<String, Long> entry : map.entrySet()) {
			sb.append("\n\taddress=").append(entry.getKey()).append(", listeners=").append(entry.getValue());
		}
		return sb.toString();
	}

	public void increment(final Map<String, Long> map, final String key) {
		final Long currentCount = map.get(key);
		map.put(key, currentCount == null ? 1 : (currentCount + 1));
	}

	public void decrement(final Map<String, Long> map, final String key) {
		final Long currentCount = map.get(key);
		map.put(key, currentCount == null || currentCount < 1 ? 0 : (currentCount - 1));
	}
}
