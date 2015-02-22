package sk.tuke.kpi.eprez.web.controllers;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;

@Controller
@Scope("request")
public class PresentationsController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Resource
	private transient PresentationDao presentationDao;

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
