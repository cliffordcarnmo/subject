package se.subject.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class UserController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	private ICredentialService credentialService;

	@Autowired
	private IUserRepository userRepository;

	@GetMapping("/edituser")
	public ModelAndView userView(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
			modelAndView.setViewName("login");
		} else {
			modelAndView.setViewName("editUser");
		}

		return modelAndView;
	}

	@GetMapping("/user/{userId}")
	public ModelAndView userDetailsView(@PathVariable("userId") Long userId, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user");

		if (userId instanceof Long){
			if(userRepository.findById(userId).isPresent()){
				modelAndView.addObject("user", userRepository.findById(userId).get());
			}else{
				modelAndView.addObject("message", messageService.getMessage("userMissingError"));
			}
		}

		return modelAndView;
	}

	@PostMapping("/user")
	public RedirectView userUpdate(@RequestBody MultiValueMap<String, String> values, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/edituser");

		String email = values.getFirst("email");
		String name = values.getFirst("name");
		String password = values.getFirst("password");
		
		User user = (User)session.getAttribute("user");

		if (user == null) {
			redirectView.setUrl("/");
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));
		} else {
			if (email.isEmpty() || name.isEmpty()) {
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsMissing"));
			} else {
				user.setEmail(email);
				user.setName(name);

				if(!password.isEmpty()){
					HashMap<String, String> credentials = new HashMap<String, String>();
					credentials = credentialService.generateCredentials(password);
					user.setSalt(credentials.get("salt"));
					user.setHash(credentials.get("hash"));
				}

				userRepository.save(user);
				session.setAttribute("user", user);
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("userUpdated"));
			}
		}

		return redirectView;
	}

	@PostMapping("/user/remove")
	public RedirectView userRemove(@RequestBody MultiValueMap<String, String> values, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();

		if (session.getAttribute("user") == null) {
			redirectView.setUrl("/");
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("userUpdateError"));
		}

		return redirectView;
	}
}