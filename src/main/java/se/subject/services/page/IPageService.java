package se.subject.services.page;

import java.util.List;

import se.subject.entities.Page;

public interface IPageService {
	public String getTree(List<Page> pages);
	public String getTree(Page page);
}