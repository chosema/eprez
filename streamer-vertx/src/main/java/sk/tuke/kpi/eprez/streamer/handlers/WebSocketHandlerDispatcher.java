package sk.tuke.kpi.eprez.streamer.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.ServerWebSocket;

public class WebSocketHandlerDispatcher implements Handler<ServerWebSocket> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandlerDispatcher.class);

	private final List<PatternBinding> bindings = new CopyOnWriteArrayList<>();
	private Handler<ServerWebSocket> noMatchHandler;

	@Override
	public void handle(final ServerWebSocket socket) {
		LOGGER.info("Received WS connection from: " + socket.remoteAddress());
		route(socket, bindings);
	}

	public WebSocketHandlerDispatcher on(final String pattern, final Handler<ServerWebSocket> handler) {
		addPattern(pattern, handler, bindings);
		return this;
	}

	public WebSocketHandlerDispatcher onWithRegEx(final String regex, final Handler<ServerWebSocket> handler) {
		addRegEx(regex, handler, bindings);
		return this;
	}

	/**
	 * Specify a handler that will be called when no other handlers match. If this handler is not specified default behaviour is to close WebSocket connection
	 */
	public WebSocketHandlerDispatcher noMatch(final Handler<ServerWebSocket> handler) {
		noMatchHandler = handler;
		return this;
	}

	private static void addPattern(final String input, final Handler<ServerWebSocket> handler, final List<PatternBinding> bindings) {
		// We need to search for any :<token name> tokens in the String and replace them with named capture groups
		final Matcher m = Pattern.compile(":([A-Za-z][A-Za-z0-9_]*)").matcher(input);
		final StringBuffer sb = new StringBuffer();
		final Set<String> groups = new HashSet<>();
		while (m.find()) {
			final String group = m.group().substring(1);
			if (groups.contains(group)) {
				throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
			}
			m.appendReplacement(sb, "(?<$1>[^\\/]+)");
			groups.add(group);
		}
		m.appendTail(sb);
		final String regex = sb.toString();
		final PatternBinding binding = new PatternBinding(Pattern.compile(regex), groups, handler);
		bindings.add(binding);
	}

	private static void addRegEx(final String input, final Handler<ServerWebSocket> handler, final List<PatternBinding> bindings) {
		final PatternBinding binding = new PatternBinding(Pattern.compile(input), null, handler);
		bindings.add(binding);
	}

	private void route(final ServerWebSocket socket, final List<PatternBinding> bindings) {
		for (final PatternBinding binding : bindings) {
			final Matcher m = binding.pattern.matcher(socket.path());
			if (m.matches()) {
				final Map<String, String> params = new HashMap<>(m.groupCount());
				if (binding.paramNames == null) { // Un-named params
					for (int i = 0; i < m.groupCount(); i++) {
						params.put("param" + i, m.group(i + 1));
					}
				} else { // Named params
					for (final String param : binding.paramNames) {
						params.put(param, m.group(param));
					}
				}
				socket.headers().add(params);
				binding.handler.handle(socket);
				return;
			}
		}
		notFound(socket);
	}

	private void notFound(final ServerWebSocket socket) {
		if (noMatchHandler != null) {
			noMatchHandler.handle(socket);
		} else {
			socket.close(); // Default close WebSocket connection
		}
	}

	private static class PatternBinding {
		final Pattern pattern;
		final Handler<ServerWebSocket> handler;
		final Set<String> paramNames;

		private PatternBinding(final Pattern pattern, final Set<String> paramNames, final Handler<ServerWebSocket> handler) {
			this.pattern = pattern;
			this.paramNames = paramNames;
			this.handler = handler;
		}
	}
}
