package se.subject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
import se.subject.repositories.IUserRepository;

@RestController
public class ValidatorController {
	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;

	@Autowired
	private IUserRepository userRepository;

	@PostMapping(path = "/validator/url")
	public ResponseEntity<Boolean> validateUrl(@RequestBody MultiValueMap<String, String> values){
		String mode = values.getFirst("mode");
		String type = values.getFirst("type");
		String url = values.getFirst("url").toLowerCase();
		String initialUrl = values.getFirst("initialUrl").toLowerCase();

		ResponseEntity<Boolean> response = ResponseEntity.ok(false);

		if (url.isBlank()) {
			response = ResponseEntity.ok(true);
		} else {
			if (type.equals("space")) {
				response = ResponseEntity.ok(spaceRepository.findByUrl(url).isPresent());
			} else if (type.equals("page")) {
				response = ResponseEntity.ok(pageRepository.findByUrl(url).isPresent());
			}
	
			if (mode.equals("edit") && url.equals(initialUrl)) {
				response = ResponseEntity.ok(false);
			}
		}

		return response;
	}

	@PostMapping("/validator/email")
	public ResponseEntity<Boolean> validateUserEmail(@RequestBody MultiValueMap<String, String> values) {
		String email = values.getFirst("email").toLowerCase();

		ResponseEntity<Boolean> response;
		response = ResponseEntity.ok(false);

		if (email.isBlank()) {
			response = ResponseEntity.ok(true);
		} else {
			response = ResponseEntity.ok(userRepository.findByEmail(email).isPresent());
		}
		
		return response;
	}
}