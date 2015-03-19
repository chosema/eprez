package sk.tuke.kpi.eprez.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityConstants {

	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	public static final GrantedAuthority ROLE_ADMIN_AUTHORITY = new SimpleGrantedAuthority(ROLE_ADMIN);
	public static final GrantedAuthority ROLE_USER_AUTHORITY = new SimpleGrantedAuthority(ROLE_USER);

	private SecurityConstants() { // private constructor
	}

}
