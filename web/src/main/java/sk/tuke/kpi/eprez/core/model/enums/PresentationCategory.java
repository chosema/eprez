package sk.tuke.kpi.eprez.core.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PresentationCategory {

	//@formatter:off
	ART("Art & Photos"),
	AUTOMOTIVE("Automotive"),
	BUSINESS("Business"),
	CAREER("Career"),
	DATA("Data & Analytics"),
	DESIGN("Design"),
	DEVICES("Devices & Hardware"),
	ECONOMY("Economy & Finance"),
	EDUCATION("Education"),
	ENGINEERING("Engineering"),
	ENTERTAINMENT("Entertainment & Humor"),
	ENVIRONMENT("Environment"),
	FOOD("Food"),
	GOVERNMENT("Government & Nonprofit"),
	HEALTH("Health & Medicine"),
	HEALTHCARE("Healthcare"),
	INTERNET("Internet"),
	INVESTOR("Investor Relations"),
	LAW("Law"),
	LEADERSHIP("Leadership & Management"),
	LIFESTYLE("Lifestyle"),
	MARKETING("Marketing"),
	MOBILE("Mobile"),
	NEWS("News & Politics"),
	PRESENTATIONS("Presentations & Public Speaking"),
	REAL("Real Estate"),
	RECRUITING("Recruiting & HR"),
	RETAIL("Retail"),
	SALES("Sales"),
	SCIENCE("Science"),
	SELF("Self Improvement"),
	SERVICES("Services"),
	SMALL("Small Business & Entrepreneurship"),
	SOCIAL("Social Media"),
	SOFTWARE("Software"),
	SPIRITUAL("Spiritual"),
	SPORTS("Sports"),
	TECHNOLOGY("Technology"),
	TRAVEL("Travel");
	//@formatter:on

	private String label;

	private PresentationCategory(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static PresentationCategory valueOfByLabel(final String label) {
		final List<PresentationCategory> filtered = Arrays.stream(values()).filter(category -> category.label.equalsIgnoreCase(label))
				.collect(Collectors.<PresentationCategory>toList());
		return filtered.size() == 0 ? null : filtered.get(0);
	}
}
