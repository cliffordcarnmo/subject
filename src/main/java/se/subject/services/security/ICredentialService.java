package se.subject.services.security;

import java.util.HashMap;
import java.util.Optional;

import se.subject.entities.User;

public interface ICredentialService {
	public Optional<User> verifyCredentials(String email, String password);
	public HashMap<String, String> generateCredentials(String password);
}