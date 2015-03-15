package sk.tuke.kpi.eprez.web.controllers;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.PresentationDao;
import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;
import sk.tuke.kpi.eprez.core.model.enums.RestrictionType;

@Controller
public class StaticsController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final RestrictionType[] RESTRICTION_TYPES = RestrictionType.values();
	private static final PresentationCategory[] PRESENTATION_CATEGORIES = PresentationCategory.values();

	private static final String IMAGE_ALLOW_TYPES = "/(\\.|\\/)(gif|jpe?g|png)$/";
	private static final String DOCUMENT_ALLOW_TYPES = "/(\\.|\\/)(gif|jpe?g|png|pdf)$/";
	private static final int DOCUMENT_SIZE_LIMIT = 10 * 1024 * 1024; // 10 MB
	private static final int IMAGE_SIZE_LIMIT = 1 * 1024 * 1024; // 1 MB

	@Autowired
	PresentationDao presentationDao;

	public RestrictionType[] getRestrictionTypes() {
		return RESTRICTION_TYPES;
	}

	public RestrictionType getRestrictionType(final String name) {
		return RestrictionType.valueOf(name);
	}

	public String getImageAllowTypes() {
		return IMAGE_ALLOW_TYPES;
	}

	public int getImageSizeLimit() {
		return IMAGE_SIZE_LIMIT;
	}

	public String getDocumentAllowTypes() {
		return DOCUMENT_ALLOW_TYPES;
	}

	public int getDocumentSizeLimit() {
		return DOCUMENT_SIZE_LIMIT;
	}

	public PresentationCategory[] getPresentationCategories() {
		return PRESENTATION_CATEGORIES;
	}
}
