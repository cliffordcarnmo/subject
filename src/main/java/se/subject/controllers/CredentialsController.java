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

package se.subject.controllers;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import se.subject.entities.User;
import se.subject.repositories.IUserRepository;
import se.subject.services.messages.IMessageService;
import se.subject.services.security.ICredentialService;

@Controller
public class CredentialsController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	ICredentialService credentialService;

	@Autowired
	IUserRepository userRepository;

	@Value("${subject.registration.enabled}")
	Boolean registrationEnabled;

	@Value("${subject.invitation.code}")
	String invitationCode;

	@GetMapping("/register")
	public ModelAndView registerView() {
		ModelAndView modelAndView = new ModelAndView();
		
		if (!registrationEnabled) {
			modelAndView.setViewName("home");
			modelAndView.addObject("message", messageService.getMessage("registrationDisabledError"));
		} else {
			modelAndView.setViewName("register");
		}

		return modelAndView;
	}

	@GetMapping("/login")
	public String loginView() {
		return "login";
	}

	@GetMapping("/logout")
	public ModelAndView homeView(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		modelAndView.addObject("message", messageService.getMessage("logoutCompleted"));
		session.removeAttribute("user");
		session.invalidate();

		return modelAndView;
	}

	@PostMapping("/register")
	public ModelAndView register(@RequestBody MultiValueMap<String, String> values, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();

		String email = values.getFirst("email");
		String name = values.getFirst("name");
		String password = values.getFirst("password");
		String invitation = values.getFirst("invitation");

		values.clear();

		if (email.isEmpty() || name.isEmpty() || password.isEmpty() || invitation.isEmpty()) {
			modelAndView.setViewName("register");
			modelAndView.addObject("message", messageService.getMessage("registrationMissingError"));
		} else {
			if(invitation.equals(invitationCode)){
				if (userRepository.findByEmail(email).isPresent()){
					modelAndView.addObject("message", messageService.getMessage("registrationUserExistsError"));
				} else {
					User user = new User();
	
					user.setEmail(email);
					user.setName(name);
	
					HashMap<String, String> credentials = credentialService.generateCredentials(password);
					password = "";
	
					user.setSalt(credentials.get("salt"));
					user.setHash(credentials.get("hash"));
					
					userRepository.save(user);
					
					user.setHash("");
					user.setSalt("");
					
					session.setAttribute("user", user);
					
					modelAndView.setViewName("home");
					modelAndView.addObject("message", messageService.getMessage("registrationCompleted"));
				}
			} else {
				modelAndView.addObject("message", messageService.getMessage("registrationMissingInvitationError"));
			}
		}
		return modelAndView;
	}

	@PostMapping("/login")
	public RedirectView login(@RequestBody MultiValueMap<String, String> values, HttpSession session, RedirectAttributes redirectAttributes ) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/login");

		String email = values.getFirst("loginEmail");
		String password = values.getFirst("password");

		values.clear();

		if (email.isBlank() || password.isBlank()) {
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsMissing"));
		} else {
			Optional<User> user = credentialService.verifyCredentials(email, password);
			password = "";

			if (user.isPresent()) {
				user.get().setHash("");
				user.get().setSalt("");
				session.setAttribute("user", user.get());
				redirectView.setUrl("/");
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsVerified"));
			} else {
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsNotFoundError"));
			}
		}

		return redirectView;
	}
}