package sk.tuke.kpi.eprez.web.controllers;

import java.io.Serializable;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.Presentation;

@Controller
@Scope("view")
public class CreatePresentationController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private transient PresentationDao presentationDao;

	String id;

	Presentation presentation;

	public void init() {
		presentation = id == null ? new Presentation() : presentationDao.findOne(id);
	}

	public String onSave() {
		presentation = presentationDao.save(presentation);
		return "create-presentation.xhtml?id=" + presentation.getId() + "&faces-redirect=true";
	}

	public void onDocumentUpload(final FileUploadEvent event) {
		final UploadedFile file = event.getFile();
		System.out.println("UPLOADED: " + file.getFileName() + " with " + file.getSize() + " bytes");
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(final Presentation presentation) {
		this.presentation = presentation;
	}

}
