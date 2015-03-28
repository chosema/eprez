package sk.tuke.kpi.eprez.streamer.model;

import java.util.Date;

public class ChatMessageDto {

	private String from;
	private String to;
	private String message;
	private Date timestamp;

	public ChatMessageDto() { // default constructor
	}

	public ChatMessageDto(final String from, final String to, final String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(final String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(final String to) {
		this.to = to;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}
}
