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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import se.subject.entities.Page;
import se.subject.entities.Space;
import se.subject.entities.User;
import se.subject.repositories.IPageRepository;
import se.subject.repositories.ISpaceRepository;
import se.subject.services.messages.IMessageService;

@Controller
public class PageController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	private ISpaceRepository spaceRepository;

	@Autowired
	private IPageRepository pageRepository;

	@GetMapping("/space/{spaceUrl}/{pageUrl}")
	public ModelAndView space(@PathVariable("spaceUrl") String spaceUrl, @PathVariable("pageUrl") String pageUrl, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("page");
		modelAndView.addObject("pageView", true);

		if (spaceRepository.findByUrl(spaceUrl).isPresent()) {
			Space space = spaceRepository.findByUrl(spaceUrl).get();
			modelAndView.addObject("space", space);

			if (pageRepository.findByUrl(pageUrl).isPresent()) {
				modelAndView.addObject("pages", pageRepository.findAllBySpaceOrderByUpdatedDesc(spaceRepository.findByUrl(spaceUrl).get()));

				Page page = pageRepository.findByUrl(pageUrl).get();
				modelAndView.addObject("page", page);

				if( !page.getComments().isEmpty()) {
					modelAndView.addObject("comments", page.getComments());
				}

				Parser parser = Parser.builder().build();
				Node document = parser.parse(page.getContent());
				HtmlRenderer renderer = HtmlRenderer.builder().build();
				modelAndView.addObject("content", renderer.render(document));
			} else {
				modelAndView.addObject("message", messageService.getMessage("pageError"));
			}
		} else {
			modelAndView.addObject("message", messageService.getMessage("spaceError"));
		}
		
		return modelAndView;
	}

	@GetMapping("/page/edit/{pageUrl}")
	public ModelAndView editPage(@PathVariable("pageUrl") String pageUrl, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("editPage");
		modelAndView.addObject("pageEdit", true);

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
		} else {
			if (pageRepository.findByUrl(pageUrl).isPresent()) {
				Page page = pageRepository.findByUrl(pageUrl).get();
				modelAndView.addObject("space", page.getSpace());
				modelAndView.addObject("page", page);
			} else {
				modelAndView.addObject("message", messageService.getMessage("pageError"));
			}
		}

		return modelAndView;
	}

	@GetMapping("/space/{spaceUrl}/page/create")
	public ModelAndView createPage(@PathVariable("spaceUrl") String spaceUrl, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createPage");

		if (session.getAttribute("user") == null) {
			modelAndView.addObject("message", messageService.getMessage("credentialsError"));
		} else {
			if (spaceRepository.findByUrl(spaceUrl).isPresent()) {
				Space space = spaceRepository.findByUrl(spaceUrl).get();
				modelAndView.addObject("space", space);
				modelAndView.addObject("page", new Page());
			} else {
				modelAndView.addObject("message", messageService.getMessage("spaceError"));
			}
		}

		return modelAndView;
	}

	@PostMapping("/page/edit")
	public RedirectView updatePage(@ModelAttribute("page") Page page, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/");

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));
		} else {
			if (page.getName().isEmpty() || page.getUrl().isEmpty()) {
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("pageUpdateMissingError"));
			} else {
				Page oldPage = pageRepository.findByUrl(page.getUrl()).get();

				oldPage.setContent(page.getContent());
				oldPage.setName(page.getName());
				oldPage.setUrl(page.getUrl());

				pageRepository.save(oldPage);

				Space space = oldPage.getSpace();
				space.setUpdated(oldPage.getUpdated());

				spaceRepository.save(space);
				
				redirectView.setUrl("/space/" + oldPage.getSpace().getUrl() + "/" + oldPage.getUrl());
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("pageUpdated"));
			}
		}

		return redirectView;
	}

	@PostMapping("/page/create")
	public RedirectView createPage(@RequestBody MultiValueMap<String, String> values, HttpSession session, RedirectAttributes redirectAttributes) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/");

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("message", messageService.getMessage("credentialsError"));

			return redirectView;
		} else {
			if (values.getFirst("name").isEmpty() || values.getFirst("url").isEmpty()) {
				redirectAttributes.addFlashAttribute("message", messageService.getMessage("pageCreationMissingError"));

				return redirectView;
			} else {
				if (pageRepository.findByUrl(values.getFirst("url")).isPresent()) {
					redirectAttributes.addFlashAttribute("message", messageService.getMessage("pageCreationUrlExistsError"));
				} else {
					User user = (User) session.getAttribute("user");
					Page page = new Page();
	
					page.setName(values.getFirst("name"));
					page.setUrl(values.getFirst("url"));
					page.setContent(values.getFirst("content"));
					page.setUser(user);
					page.setSpace(spaceRepository.findById((Integer.parseInt(values.getFirst("spaceid")))).get());
	
					Page createdPage = pageRepository.save(page);
	
					Space space = createdPage.getSpace();
					space.setUpdated(createdPage.getUpdated());
	
					spaceRepository.save(space);
	
					redirectView.setUrl("/space/" + createdPage.getSpace().getUrl() + "/" + createdPage.getUrl());
					redirectAttributes.addFlashAttribute("user", user);
					redirectAttributes.addFlashAttribute("message", messageService.getMessage("pageCreated"));
				}
			}
		}
		return redirectView;
	}
}