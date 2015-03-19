package sk.tuke.kpi.eprez.streamer.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenParser {

	private final Pattern pattern;

	public TokenParser(final String prefix) {
		this.pattern = Pattern.compile("^" + Pattern.quote(prefix) + "([a-z0-9]{24})" + "$");
	}

	public String parse(final String uri) {
		final Matcher matcher = pattern.matcher(uri);
		return matcher.matches() ? matcher.group(1) : null;
	}
}
