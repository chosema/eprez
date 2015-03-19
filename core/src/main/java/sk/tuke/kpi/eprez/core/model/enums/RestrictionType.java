package sk.tuke.kpi.eprez.core.model.enums;

public enum RestrictionType {

	PUBLIC("Public free", true), REGISTERED("Only registered", false), PROTECTED("Password protected", false);

	private final String label;
	private final boolean free;

	private RestrictionType(final String label, final boolean free) {
		this.label = label;
		this.free = free;
	}

	public boolean isFree() {
		return free;
	}

	public String getLabel() {
		return label;
	}
}
