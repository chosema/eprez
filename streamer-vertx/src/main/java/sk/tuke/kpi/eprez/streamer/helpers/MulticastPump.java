package sk.tuke.kpi.eprez.streamer.helpers;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;

public class MulticastPump {

	private final ReadStream<?> readStream;

	private final List<WriteStream<?>> writeStreams = new ArrayList<>();

	private int defaultWriteQueueMaxSize = -1;

	private int pumped;

//	private final Handler<Void> drainHandler = new Handler<Void>() {
//		@Override
//		public void handle(final Void v) {
//			readStream.resume();
//		}
//	};

	private final Handler<Buffer> dataHandler = buffer -> {
		for (final WriteStream<?> ws : writeStreams) {
			if (!ws.writeQueueFull()) {
				ws.write(buffer);
			}
		}
		pumped += buffer.length();
	};

	private MulticastPump(final ReadStream<?> rs, final WriteStream<?> ws, final int maxWriteQueueSize) {
		this(rs, ws);
		this.defaultWriteQueueMaxSize = maxWriteQueueSize;
		ws.setWriteQueueMaxSize(maxWriteQueueSize);
	}

	private MulticastPump(final ReadStream<?> rs, final WriteStream<?> ws) {
		this.writeStreams.add(ws);
		this.readStream = rs;
	}

	public static MulticastPump createPump(final ReadStream<?> rs, final WriteStream<?> ws) {
		return new MulticastPump(rs, ws);
	}

	public static MulticastPump createPump(final ReadStream<?> rs, final WriteStream<?> ws, final int writeQueueMaxSize) {
		return new MulticastPump(rs, ws, writeQueueMaxSize);
	}

	public MulticastPump add(final WriteStream<?> ws) {
		writeStreams.add(ws);
		if (defaultWriteQueueMaxSize != -1) {
			ws.setWriteQueueMaxSize(defaultWriteQueueMaxSize);
		}
		return this;
	}

	public MulticastPump add(final WriteStream<?> ws, final int writeQueueMaxSize) {
		ws.setWriteQueueMaxSize(writeQueueMaxSize);
		return add(ws);
	}

	public MulticastPump remove(final WriteStream<?> ws) {
		writeStreams.remove(ws);
		return this;
	}

	public int getWriteStreamsCount() {
		return writeStreams.size();
	}

	public MulticastPump start() {
		readStream.dataHandler(dataHandler);
		return this;
	}

	public MulticastPump stop() {
		readStream.dataHandler(null);
		for (final WriteStream<?> writeStream : writeStreams) {
			writeStream.drainHandler(null);
		}
		return this;
	}

	public int bytesPumped() {
		return pumped;
	}
}
