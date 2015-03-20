package sk.tuke.kpi.eprez.streamer.helpers;

import java.util.ArrayList;
import java.util.List;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.streams.WriteStream;

public abstract class AbstractMulticastPump {

	protected final List<WriteStream<?>> writeStreams = new ArrayList<>();

	protected int defaultWriteQueueMaxSize;
	protected int pumped;

//	private final Handler<Void> drainHandler = new Handler<Void>() {
//		@Override
//		public void handle(final Void v) {
//			readStream.resume();
//		}
//	};

	public AbstractMulticastPump() {
		this(-1);
	}

	public AbstractMulticastPump(final int defaultWriteQueueMaxSize) {
		this.defaultWriteQueueMaxSize = defaultWriteQueueMaxSize;
	}

	protected final Handler<Buffer> dataHandler = buffer -> {
		for (final WriteStream<?> ws : writeStreams) {
			if (!ws.writeQueueFull()) {
				ws.write(buffer);
			}
		}
		pumped += buffer.length();
	};

	public AbstractMulticastPump add(final WriteStream<?> ws) {
		writeStreams.add(ws);
		if (defaultWriteQueueMaxSize != -1) {
			ws.setWriteQueueMaxSize(defaultWriteQueueMaxSize);
		}
		return this;
	}

	public AbstractMulticastPump add(final WriteStream<?> ws, final int writeQueueMaxSize) {
		ws.setWriteQueueMaxSize(writeQueueMaxSize);
		return add(ws);
	}

	public AbstractMulticastPump remove(final WriteStream<?> ws) {
		writeStreams.remove(ws);
		return this;
	}

	public int getWriteStreamsCount() {
		return writeStreams.size();
	}

	public abstract AbstractMulticastPump start();

	public abstract AbstractMulticastPump stop();

	public int bytesPumped() {
		return pumped;
	}
}
