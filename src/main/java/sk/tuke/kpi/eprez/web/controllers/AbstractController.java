package sk.tuke.kpi.eprez.web.controllers;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import sk.tuke.kpi.eprez.core.dao.UserDao;
import sk.tuke.kpi.eprez.core.model.User;

public abstract class AbstractController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	UserDao userDao;

	public static final String GLOBAL_MESSAGES = "globalMessages";
	public static final String GROWL_MESSAGES = null;

	protected User loggedUser;

	public void init() {
		loggedUser = getLoggedUser();
	}

	protected void showMessage(final String clientId, final Severity severity, final String summary, final String detail) {
		final FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(clientId, message);
	}

	protected void showInfoMessage(final String clientId, final String detail) {
		showMessage(clientId, FacesMessage.SEVERITY_INFO, "Info:", detail);
	}

	protected void showWarnMessage(final String clientId, final String detail) {
		showMessage(clientId, FacesMessage.SEVERITY_WARN, "Warning:", detail);
	}

	protected void showErrorMessage(final String clientId, final String detail) {
		showMessage(clientId, FacesMessage.SEVERITY_ERROR, "Error:", detail);
	}

	protected void flashMessages() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	protected String facesRedirect(final String outcome) {
		flashMessages();
		return outcome + (outcome.endsWith(".xhtml") ? "?" : "&") + "faces-redirect=true";
	}

	protected String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	protected User getLoggedUser() {
		return userDao.findByLogin(getLoggedUserLogin());
	}

	protected String getLoggedUserLogin() {
		final Authentication authentication = getAuthentication();
		return authentication == null ? null : authentication.getName();
	}

	private Authentication getAuthentication() {
		final SecurityContext context = SecurityContextHolder.getContext();
		return context == null ? null : context.getAuthentication();
	}
}
