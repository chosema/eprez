package sk.tuke.kpi.eprez.streamer;

public final class EventBusAddressHolder {

	public static final String MAIN_PREFIX = "eprez.streamer.";

	private EventBusAddressHolder() { // default constructor
	}

	public static final String presentationAudioStream(final String presentationId) {
		return MAIN_PREFIX + "presentationAudioStream:" + presentationId;
	}

	public static String presentationDocumentStream(final String presentationId) {
		return MAIN_PREFIX + "presentationDocumentStream:" + presentationId;
	}
}
