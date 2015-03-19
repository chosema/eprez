package sk.tuke.kpi.eprez.streamer.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusMap<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;

	public interface Listener<V> {
		void handle(V value);
	}

	public interface ListenerRegistration {
		boolean remove();
	}

	private final Map<K, List<Listener<V>>> eventBus = new HashMap<>();

	public ListenerRegistration register(final K key, final Listener<V> listener) {
		final List<Listener<V>> keyListeners;
		if (eventBus.containsKey(key)) {
			keyListeners = eventBus.get(key);
		} else {
			keyListeners = new ArrayList<EventBusMap.Listener<V>>();
			eventBus.put(key, keyListeners);
		}
		keyListeners.add(listener);

		return () -> keyListeners.remove(listener);
	}

	public void publish(final K key, final V value) {
		getEventBusListeners(key).forEach(listener -> listener.handle(value));
	}

	protected List<Listener<V>> getEventBusListeners(final K key) {
		return eventBus.containsKey(key) ? eventBus.get(key) : Collections.emptyList();
	}
}
