package se.subject.services.page;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import se.subject.entities.Page;

@Service
public class PageService implements IPageService {
	
	public String getTree(List<Page> pages) {
		return buildTree(pages);
	}

	public String getTree(Page page) {
		return buildTree(new ArrayList<Page>() {
			{
				add(page);
			}
		});
	}

	private String buildTree(List<Page> pages) {
		String tree = "<ul>";

		for (Page page : pages) {
			tree += "<li><a href=" + page.getPermalink() + ">" + page.getName() + "</a></li>";
			
			if(page.getChildren().size() > 0) {
				tree += buildTree(page.getChildren());
			}
		}

		tree += "</ul>";
		return tree;
	}
}