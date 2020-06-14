package se.subject.services.messages;

public class Message {
	private MessageType messageType;
	private String message;

	public static enum MessageType {
		success,
		warning,
		danger
	}

	public Message(MessageType messageType, String message){
		this.messageType = messageType;
		this.message = message;
	}

	public MessageType getMessageType(){
		return this.messageType;
	}

	public void setMessageType(MessageType messageType){
		this.messageType = messageType;
	}

	public String getMessage(){
		return this.message;
	}

	public void setMessage(String message){
		this.message = message;
	}
}