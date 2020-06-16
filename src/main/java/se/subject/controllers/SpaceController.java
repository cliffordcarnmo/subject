package se.subject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import se.subject.entities.Space;
import se.subject.entities.User;
import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
import se.subject.services.messages.IMessageService;

@Controller
public class SpaceController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;

	@GetMapping("/space/create")
	public ModelAndView createSpace(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createSpace");

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
		}else{
			modelAndView.addObject("newSpace", new Space());
		}

		return modelAndView;
	}

	@GetMapping("/space/{spaceId}")
	public ModelAndView space(@PathVariable("spaceId") Long spaceId, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("space");

		if(spaceRepository.findById(spaceId).isPresent()){
			modelAndView.addObject("pages", pageRepository.findAllByActiveTrueAndSpaceOrderByUpdatedDesc(spaceRepository.findById(spaceId).get()));
			modelAndView.addObject("space", spaceRepository.findById(spaceId).get());
		}else{
			modelAndView.addObject("message", messageService.getMessage("spaceError"));
		}

		return modelAndView;
	}

	@PostMapping("/space")
	public RedirectView saveSpace(@ModelAttribute("newSpace") Space newSpace, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();

		if (session.getAttribute("user") == null) {
			redirectView.setUrl("/");
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));
		} else {
			if (newSpace.getName().isEmpty()) {
				redirectView.setUrl("/space/create");
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceCreationMissingError"));
			} else {
				User user = (User)session.getAttribute("user");
				
				newSpace.setUserid(user.getUserid());
				newSpace.setActive(true);

				Space createdSpace = spaceRepository.save(newSpace);
				
				redirectView.setUrl("/space/" + createdSpace.getSpaceid());
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("spaceCreated"));
			}
		}

		return redirectView;
	}
}