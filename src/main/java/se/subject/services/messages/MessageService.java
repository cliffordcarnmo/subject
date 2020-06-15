package se.subject.services.messages;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import se.subject.services.messages.Message.MessageType;

@Service
public class MessageService implements IMessageService {
	private Map<String, Message> messages = new HashMap<String, Message>() {
		private static final long serialVersionUID = 1L;
		{
			put("credentialsMissing", new Message(MessageType.danger, "Credentials missing."));
			put("credentialsNotFoundError", new Message(MessageType.danger, "User not registered."));
			put("credentialsError", new Message(MessageType.danger, "You need to be registered and logged in to perform the request action."));
			put("registrationMissingError", new Message(MessageType.danger, "Missing credentials. Cannot register."));
			put("spaceError", new Message(MessageType.danger, "The space does not exist."));
			put("pageError", new Message(MessageType.danger, "The page does not exist."));
			put("registrationMissingError", new Message(MessageType.danger, "Credentials missing."));
			put("registrationMissingInvitationError", new Message(MessageType.danger, "Invalid invitation code."));
			put("spaceCreationMissingError", new Message(MessageType.danger, "Missing space name."));
			put("pageCreationMissingError", new Message(MessageType.danger, "Missing page name."));
			put("pageUpdateMissingError", new Message(MessageType.danger, "Missing page name."));
			put("userMissingError", new Message(MessageType.danger, "The user does not exist."));

			put("logoutCompleted", new Message(MessageType.success, "You were successfully logged out."));
			put("userUpdated", new Message(MessageType.success, "The user was successfully updated."));
			put("pageUpdated", new Message(MessageType.success, "The page was updated."));
			put("credentialsVerified", new Message(MessageType.success, "Credentials verified. Welcome to Subject."));
			put("registrationCompleted", new Message(MessageType.success, "Registration complete. Welcome to Subject."));
			put("spaceCreated", new Message(MessageType.success, "The space was created."));
		}
	};

	@Override
	public Message getMessage(String messageKey){
		return messages.get(messageKey);
	}
}