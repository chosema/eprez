package sk.tuke.kpi.eprez.streamer.helpers;

import java.text.DecimalFormat;
import java.util.function.Function;

public class Formatter {

	private static final double K = 1024;
	private static final double M = K * K;
	private static final double G = M * K;
	private static final double T = G * K;

	private static final DecimalFormat FORMAT = new DecimalFormat("#.##");
	private static final Function<Long, String> K_F = bytes -> FORMAT.format(bytes / K) + " KB";
	private static final Function<Long, String> M_M = bytes -> FORMAT.format(bytes / M) + " MB";
	private static final Function<Long, String> G_F = bytes -> FORMAT.format(bytes / G) + " GB";
	private static final Function<Long, String> T_F = bytes -> FORMAT.format(bytes / T) + " TB";

	public static String bytes(final long bytes) {
		return bytes >= K ? bytes >= M ? bytes >= G ? bytes >= T ? T_F.apply(bytes) : G_F.apply(bytes) : M_M.apply(bytes) : K_F.apply(bytes) : (bytes + " B");
	}
}
