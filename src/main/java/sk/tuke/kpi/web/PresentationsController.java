package sk.tuke.kpi.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.core.dao.PresentationDao;
import sk.tuke.kpi.core.model.Presentation;

@Controller
@Scope("request")
public class PresentationsController {

	@Resource
	private PresentationDao presentationDao;

	private Page<Presentation> presentations;

	private int page = 0;
	private int size = 10;

	public Page<Presentation> getPresentations() {
		return presentations == null ? (presentations = presentationDao.findAll(new PageRequest(page, size))) : presentations;
	}

	public int getPage() {
		return page;
	}

	public void setPage(final int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

}
