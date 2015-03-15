package sk.tuke.kpi.eprez.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import sk.tuke.kpi.eprez.core.dao.UserDao;
import sk.tuke.kpi.eprez.core.model.User;

@Component
public class LoggedUserProvider implements AuditorAware<User> {

	@Autowired
	UserDao userDao;

	@Override
	public User getCurrentAuditor() {
		return getUser();
	}

	public User getUser() {
		return getUser(SecurityContextHolder.getContext().getAuthentication());
	}

	public User getUser(final Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		} else {
			return userDao.findByLogin(authentication.getName());
		}
	}

	public static String getUserLogin() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		} else {
			return authentication.getName();
		}
	}

}
