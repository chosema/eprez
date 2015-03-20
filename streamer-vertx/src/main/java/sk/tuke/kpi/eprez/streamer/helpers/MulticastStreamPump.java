package sk.tuke.kpi.eprez.streamer.helpers;

import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;

public class MulticastStreamPump extends AbstractMulticastPump {

	private final ReadStream<?> readStream;

	private MulticastStreamPump(final ReadStream<?> rs, final int maxWriteQueueSize) {
		super(maxWriteQueueSize);
		this.readStream = rs;
	}

	private MulticastStreamPump(final ReadStream<?> rs) {
		this.readStream = rs;
	}

	public static MulticastStreamPump createPump(final ReadStream<?> rs) {
		return new MulticastStreamPump(rs);
	}

	public static MulticastStreamPump createPump(final ReadStream<?> rs, final int writeQueueMaxSize) {
		return new MulticastStreamPump(rs, writeQueueMaxSize);
	}

	@Override
	public MulticastStreamPump start() {
		readStream.dataHandler(dataHandler);
		return this;
	}

	@Override
	public MulticastStreamPump stop() {
		readStream.dataHandler(null);
		for (final WriteStream<?> writeStream : writeStreams) {
			writeStream.drainHandler(null);
		}
		return this;
	}
}
