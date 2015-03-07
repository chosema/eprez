package sk.tuke.kpi.eprez.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;
import sk.tuke.kpi.eprez.core.model.enums.RestrictionType;

@Controller
@Scope("request")
public class CreatePresentationController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Autowired
	PresentationDao presentationDao;

	String name;

	public String onCreate() {
		final Presentation saved = presentationDao.save(new Presentation(name, RestrictionType.PUBLIC));
		showInfoMessage(GLOBAL_MESSAGES, "Presentation '" + saved.getName() + "' was sucessfully created.");
		return facesRedirect("view-presentation.xhtml?id=" + saved.getId());
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
