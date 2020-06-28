package se.subject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

	@PostMapping(path = "/validator/spaceUrl")
	public ResponseEntity<Boolean> validateSpaceUrl(@RequestBody MultiValueMap<String, String> values){
		if (spaceRepository.findByUrl(values.getFirst("url")).isPresent()) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}

	@PostMapping("/validator/pageUrl")
	public ResponseEntity<Boolean> validatePageUrl(@RequestBody MultiValueMap<String, String> values) {
		if (pageRepository.findByUrl(values.getFirst("url")).isPresent()) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}

	@PostMapping("/validator/userEmail")
	public ResponseEntity<Boolean> validateUserEmail(@RequestBody MultiValueMap<String, String> values) {
		if (userRepository.findByEmail(values.getFirst("email")).isPresent()) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}
}