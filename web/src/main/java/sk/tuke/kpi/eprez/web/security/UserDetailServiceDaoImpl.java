package sk.tuke.kpi.eprez.web.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import sk.tuke.kpi.eprez.core.dao.UserDao;
import sk.tuke.kpi.eprez.core.model.User;

@Component("userDetailsService")
public class UserDetailServiceDaoImpl implements UserDetailsService {

	@Autowired
	UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userDao.findByLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException("User with login " + username + " was not found in UserDao repository");
		}
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), transform(user.getRoles()));
	}

	private Collection<SimpleGrantedAuthority> transform(final Collection<String> roles) {
		return roles == null ? Collections.emptyList() : roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}
}
