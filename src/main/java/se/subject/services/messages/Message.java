/*
    Subject
    Copyright (C) 2020 Clifford Carnmo

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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