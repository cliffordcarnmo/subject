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

import se.subject.entities.Comment;
import se.subject.entities.Page;
import se.subject.entities.Space;
import se.subject.entities.User;
import se.subject.repositories.ICommentRepository;
import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
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

	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;

	@Autowired
	private ICommentRepository commentRepository;

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

	@GetMapping("/user/{userEmail}")
	public ModelAndView userDetailsView(@PathVariable("userEmail") String userEmail, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user");

		if(userRepository.findByEmail(userEmail).isPresent()){
			User user = userRepository.findByEmail(userEmail).get();
			modelAndView.addObject("user", user);
			modelAndView.addObject("pages", pageRepository.findAllByUserOrderByUpdatedDesc(user));
		}else{
			modelAndView.addObject("message", messageService.getMessage("userMissingError"));
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
				
				user.setHash("");
				user.setSalt("");
				
				session.setAttribute("user", user);
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("userUpdated"));
			}
		}

		return redirectView;
	}

	@PostMapping("/user/remove")
	public RedirectView userRemove(HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/");

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("userRemoveError"));
		} else {
			User user = (User)session.getAttribute("user");
			
			for (Page page : pageRepository.findAllByUser(user)) {
				for (Comment comment : page.getComments()) {
					commentRepository.delete(comment);
				}

				pageRepository.delete(page);
			}
			
			for (Space space : user.getSpaces()) {
				spaceRepository.delete(space);
			}
			
			userRepository.delete(user);

			session.removeAttribute("user");
			session.invalidate();

			redirectAttributes.addFlashAttribute("message", messageService.getMessage("userRemoved"));
		}

		return redirectView;
	}
}