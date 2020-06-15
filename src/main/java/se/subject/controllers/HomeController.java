package se.subject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
import se.subject.repositories.IUserRepository;

@Controller
public class HomeController {
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;
	
	@GetMapping("/")
	public ModelAndView home(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("userRepository", userRepository);
		modelAndView.addObject("spaces", spaceRepository.findAll());
		modelAndView.addObject("pageRepository", pageRepository);

		return modelAndView;
	}
}