package sk.tuke.kpi.eprez.web.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import sk.tuke.kpi.eprez.core.dao.UserDao;
import sk.tuke.kpi.eprez.core.model.User;

@Controller
@Scope("request")
public class SignUpController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Autowired
	UserDao userDao;

	User user = new User();

	String passwordAgain;

	public String onSign() {
		if (!StringUtils.equals(user.getPassword(), passwordAgain)) {
			showErrorMessage("passwordCheck", "Passwords must match");
		}

		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.getRoles().add("USER");
		userDao.save(user);

		final String loginLink = "<a href=\"" + getContextPath() + "/login\">here</a>";
		showInfoMessage(GLOBAL_MESSAGES, "You have been sucessfully registered. You can login " + loginLink + " now.");
		return facesRedirect("overview.xhtml");
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public String getPasswordAgain() {
		return passwordAgain;
	}

	public void setPasswordAgain(final String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

}
