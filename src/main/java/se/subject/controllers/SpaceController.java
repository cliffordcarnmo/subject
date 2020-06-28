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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import se.subject.entities.Space;
import se.subject.entities.User;
import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
import se.subject.services.logging.ILoggingService;
import se.subject.services.messages.IMessageService;

@Controller
public class SpaceController {
	@Autowired
	ILoggingService loggingService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;

	@GetMapping("/space/")
	public ModelAndView allSpaces(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("spaces");
		modelAndView.addObject("spaces",spaceRepository.findAllByOrderByUpdatedDesc());		

		//loggingService.Log(new HashMap<String, Object>() {{ put("class", this.getClass()); put("session", session); put("model", modelAndView);};});

		return modelAndView;
	}

	@GetMapping("/space/create")
	public ModelAndView createSpace(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createSpace");

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
		}else{
			modelAndView.addObject("newSpace", new Space());
		}

		//loggingService.Log(new HashMap<String, Object>() {{ put("class", this.getClass()); put("session", session); put("model", modelAndView);};});

		return modelAndView;
	}

	@GetMapping("/space/edit/{spaceUrl}")
	public ModelAndView editSpace(@PathVariable("spaceUrl") String spaceUrl, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("editSpace");
		modelAndView.addObject("spaceEdit", true);

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
		} else {
			if (spaceRepository.findByUrl(spaceUrl).isPresent()) {
				Space space = spaceRepository.findByUrl(spaceUrl).get();
				modelAndView.addObject("space", space);
			} else {
				modelAndView.addObject("message", messageService.getMessage("spaceError"));
			}
		}

		//loggingService.Log(new HashMap<String, Object>() {{ put("class", this.getClass()); put("session", session); put("model", modelAndView);};});

		return modelAndView;
	}

	@GetMapping("/space/{spaceUrl}")
	public ModelAndView space(@PathVariable("spaceUrl") String spaceUrl, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("space");
		modelAndView.addObject("spaceView", true);

		if(spaceRepository.findByUrl(spaceUrl).isPresent()){
			Space space = spaceRepository.findByUrl(spaceUrl).get();
			modelAndView.addObject("space", space);
			modelAndView.addObject("allPages", pageRepository.findAllBySpaceOrderByUpdatedDesc(space));
		}else{
			modelAndView.addObject("message", messageService.getMessage("spaceError"));
		}

		//loggingService.Log(new HashMap<String, Object>() {{ put("class", this.getClass()); put("session", session); put("model", modelAndView);};});

		return modelAndView;
	}

	@PostMapping("/space/create")
	public RedirectView createSpace(@ModelAttribute("newSpace") Space space, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();

		if (session.getAttribute("user") == null) {
			redirectView.setUrl("/");
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));
		} else {
			if (space.getName().isEmpty() || space.getUrl().isEmpty()) {
				redirectView.setUrl("/space/create");
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceCreationMissingError"));
			} else {
				User user = (User)session.getAttribute("user");
				List<User> users = new ArrayList<User>();

				users.add(user);
				space.setUsers(users);

				Space createdSpace = spaceRepository.save(space);
				
				redirectView.setUrl("/space/" + createdSpace.getUrl());
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceCreated"));
			}
		}

		return redirectView;
	}

	@PostMapping("/space/edit")
	public RedirectView editSpace(@ModelAttribute("space") Space space, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();

		if (session.getAttribute("user") == null) {
			redirectView.setUrl("/");
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));
		} else {
			if (space.getName().isEmpty() || space.getUrl().isEmpty()) {
				redirectView.setUrl("/");
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceUpdateMissingError"));
			} else {

				Space oldSpace = spaceRepository.findByUrl(space.getUrl()).get();

				oldSpace.setName(space.getName());
				oldSpace.setDescription(space.getDescription());
				
				spaceRepository.save(oldSpace);

				redirectView.setUrl("/space/edit/" + oldSpace.getUrl());
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceUpdated"));
			}
		}

		return redirectView;
	}

}