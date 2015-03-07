package sk.tuke.kpi.eprez.web.controllers;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.web.data.AbstractSpringLazyDataModel;
import sk.tuke.kpi.eprez.web.data.PageRequest;

@Controller
@Scope("view")
public class FeaturedPresentationsController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Autowired
	PresentationDao presentationDao;

	List<PresentationCategory> categories;

	private final LazyDataModel<Presentation> presentations = new AbstractSpringLazyDataModel<Presentation>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected Page<Presentation> getData(final PageRequest pageRequest) {
			return presentationDao.findAll(convert(pageRequest));
		}
	};

	public void init() {

	}

	public List<PresentationCategory> getCategories() {
		return categories;
	}

	public void setCategories(final List<PresentationCategory> categories) {
		this.categories = categories;
	}

	public LazyDataModel<Presentation> getPresentations() {
		return presentations;
	}

	public StreamedContent getImage(final Presentation presentation) {
		return presentation.getImage().length == 0 ? null : new ByteArrayContent(presentation.getImage());
	}

	public boolean hasImage(final Presentation presentation) {
		return ArrayUtils.isNotEmpty(presentation.getImage());
	}
}
