package se.subject.services.logging;

public class LoggingEvent {
	private String title;
	private Object content;

	public LoggingEvent(String title, Object content) {
		this.title = title;
		this.content = content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setContent(Object content) {
		this.content = content;;
	}

	public Object getContent() {
		return this.content;
	}
}