package sk.tuke.kpi.eprez.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource(name = "userDetailsService")
	UserDetailsService userDetails;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetails).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		//@formatter:off
		http.csrf().disable()
			.authorizeRequests().antMatchers("/presentation.xhtml").authenticated().and()
			.authorizeRequests().antMatchers("/streamer/**").authenticated().and()
			.authorizeRequests().antMatchers("/**").permitAll().and()
			.formLogin().permitAll().and()
			.logout().logoutSuccessUrl("/").permitAll();
		//@formatter:on
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
	}

//	@Configuration
//	@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//	public static class MethodSecurity extends GlobalMethodSecurityConfiguration {
//	}

}
