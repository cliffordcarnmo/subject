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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//import java.util.HashMap;

import javax.servlet.http.HttpSession;

import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
//import se.subject.services.logging.ILoggingService;

@Controller
public class HomeController {
	//@Autowired
	//private ILoggingService loggingService;

	@Autowired
	private ISpaceRepository spaceRepository;
	
	@Autowired
	private IPageRepository pageRepository;

	@GetMapping("/")
	public ModelAndView home(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("homeView",true);
		modelAndView.addObject("spaces", spaceRepository.findTop10ByOrderByUpdatedDesc());		
		modelAndView.addObject("pageRepository", pageRepository);
		
		//loggingService.Log(new HashMap<String, Object>() {{ put("class", this.getClass()); put("session", session); put("model", modelAndView);};});
		
		return modelAndView;
	}
}