package sk.tuke.kpi.eprez.core.model.enums;

public enum RestrictionType {

	PUBLIC("Public free"), REGISTERED("Only registered"), PROTECTED("Password protected");

	private final String label;

	private RestrictionType(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
