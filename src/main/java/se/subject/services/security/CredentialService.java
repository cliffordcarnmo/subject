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

package se.subject.services.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.subject.entities.User;
import se.subject.repositories.IUserRepository;

@Service
public class CredentialService implements ICredentialService {
	@Autowired
	private IUserRepository userRepository;

	private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
	private static final int KEY_LENGTH = 512;
	private static final int ITERATIONS = 65536;
	
	@Override
	public HashMap<String, String> generateCredentials(String password) {
		HashMap<String, String> credentials = new HashMap<String, String>();
		String salt = generateSalt(KEY_LENGTH);
		String hash = generateHash(password, salt).get();

		password = "";

		credentials.put("salt", salt);
		credentials.put("hash", hash);

		return credentials;
	}

	@Override
	public Optional<User> verifyCredentials(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);

		if (user.isPresent()) {
			Optional<String> encrypted = generateHash(password, user.get().getSalt());

			password = "";

			if (!encrypted.isPresent()) {
				return Optional.empty();
			} else {
				if(encrypted.get().equals(user.get().getHash())){
					return user;
				}else{
					return Optional.empty();
				}
			}
		} else {
			return Optional.empty();
		}
	}

	private static String generateSalt(final int length) {
		final SecureRandom RAND = new SecureRandom();
		byte[] salt = new byte[length];
		RAND.nextBytes(salt);

		return Base64.getEncoder().encodeToString(salt);
	}

	private static Optional<String> generateHash(String password, String salt) {
		char[] chars = password.toCharArray();
		byte[] bytes = salt.getBytes();

		PBEKeySpec keySpec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

		Arrays.fill(chars, Character.MIN_VALUE);
		password = "";

		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			byte[] securePassword = secretKeyFactory.generateSecret(keySpec).getEncoded();
			return Optional.of(Base64.getEncoder().encodeToString(securePassword));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			return Optional.empty();
		} finally {
			keySpec.clearPassword();
		}
	}
}