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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import se.subject.entities.Comment;
import se.subject.entities.Page;
import se.subject.entities.User;
import se.subject.repositories.ICommentRepository;
import se.subject.repositories.IPageRepository;
import se.subject.services.logging.ILoggingService;
import se.subject.services.messages.IMessageService;

@Controller
public class CommentController {
	@Autowired
	ILoggingService loggingService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IPageRepository pageRepository;

	@Autowired
	private ICommentRepository commentRepository;

	@PostMapping("/comment/create")
	public RedirectView createComment(@RequestBody MultiValueMap<String, String> values, HttpSession session) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/");

		if (session.getAttribute("user") == null) {
			redirectView.addStaticAttribute("message", messageService.getMessage("credentialsError"));

			return redirectView;
		}

		if (values.getFirst("content").isBlank() || values.getFirst("pageurl").isBlank()) {
			redirectView.addStaticAttribute("message", messageService.getMessage("createCommentMissingError"));

			return redirectView;
		}

		String content = values.getFirst("content");
		String pageUrl = values.getFirst("pageurl");

		Page page = pageRepository.findByUrl(pageUrl).get();
		User user = (User)session.getAttribute("user");

		Comment comment = new Comment();
		comment.setUser(user);
		comment.setPage(page);
		comment.setContent(content);

		if (values.containsKey("parentid") && (!values.getFirst("parentid").isBlank() || !values.getFirst("parentid").isBlank())) {
			comment.setParentid(Integer.parseInt(values.getFirst("parentid")));
		}

		commentRepository.save(comment);

		redirectView.setUrl("/space/" + page.getSpace().getUrl() + "/" + page.getUrl());
		redirectView.addStaticAttribute("message", messageService.getMessage("createCommentSuccess"));

		return redirectView;
	}
}