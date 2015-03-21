package sk.tuke.kpi.eprez.streamer.pumps;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.streams.WriteStream;

public abstract class AbstractMulticastPump {

	public static final String UNKNOWN_IDENTIFIER = "N/A";

	protected class Listener {

		private final WriteStream<?> writeStream;
		private final String identifier;

		public Listener(final WriteStream<?> writeStream) {
			this(writeStream, UNKNOWN_IDENTIFIER);
		}

		public Listener(final WriteStream<?> writeStream, final String identifier) {
			this.writeStream = writeStream;
			this.identifier = identifier;
		}

		public WriteStream<?> getWriteStream() {
			return writeStream;
		}

		public String getIdentifier() {
			return identifier;
		}

		@Override
		public String toString() {
			return "Listener [" + identifier + "]";
		}
	}

	protected final List<Listener> listeners = new ArrayList<>();

	protected int defaultWriteQueueMaxSize;
	protected int pumped;

	protected final Handler<Buffer> dataHandler = buffer -> {
		for (final Listener listener : listeners) {
			if (!listener.getWriteStream().writeQueueFull()) {
				listener.getWriteStream().write(buffer);
			}
		}
		pumped += buffer.length();
	};

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

	public AbstractMulticastPump add(final WriteStream<?> ws) {
		return add(new Listener(ws));
	}

	public AbstractMulticastPump add(final WriteStream<?> ws, final String identifier) {
		return add(new Listener(ws, identifier));
	}

	protected AbstractMulticastPump add(final Listener listener) {
		listeners.add(listener);
		if (defaultWriteQueueMaxSize != -1) {
			listener.getWriteStream().setWriteQueueMaxSize(defaultWriteQueueMaxSize);
		}
		return this;
	}

	public AbstractMulticastPump remove(final WriteStream<?> ws) {
		listeners.removeAll(listeners.stream().filter(listener -> listener.writeStream.equals(ws)).collect(Collectors.toList()));
		return this;
	}

	public AbstractMulticastPump remove(final String identifier) {
		listeners.removeAll(listeners.stream().filter(listener -> listener.identifier.equals(identifier)).collect(Collectors.toList()));
		return this;
	}

	public int getListenersCount() {
		return listeners.size();
	}

	public abstract AbstractMulticastPump start();

	public abstract AbstractMulticastPump stop();

	public int bytesPumped() {
		return pumped;
	}
}
