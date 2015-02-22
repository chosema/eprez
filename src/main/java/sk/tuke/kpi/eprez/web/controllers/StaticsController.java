package sk.tuke.kpi.eprez.web.controllers;

import java.io.Serializable;

import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.model.RestrictionType;

@Controller
public class StaticsController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final RestrictionType[] RESTRICTION_TYPES = RestrictionType.values();

	private static final String DOCUMENT_ALLOW_TYPES = "/(\\.|\\/)(gif|jpe?g|png|pdf)$/";
	private static final int DOCUMENT_SIZE_LIMIT = 10000 * 1024; // kB * 1024 => B

	public RestrictionType[] getRestrictionTypes() {
		return RESTRICTION_TYPES;
	}

	public RestrictionType getRestrictionType(final String name) {
		return RestrictionType.valueOf(name);
	}

	public String getDocumentAllowTypes() {
		return DOCUMENT_ALLOW_TYPES;
	}

	public int getDocumentSizeLimit() {
		return DOCUMENT_SIZE_LIMIT;
	}

}
