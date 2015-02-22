package sk.tuke.kpi.eprez.core.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User implements HasId<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Indexed(unique = true)
	@NotNull
	private String login;
	private String firstName;
	private String lastName;
	@NotNull
	private String password;
	@NotNull
	private String email;

	private Set<String> roles;

	@DBRef(lazy = true)
	private List<Presentation> presentations;

	public User() { // default constructor
	}

	public User(final String login, final String firstName, final String lastName) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(final String login, final String password, final Set<String> roles) {
		this.login = login;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(final Set<String> roles) {
		this.roles = roles;
	}

	public List<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(final List<Presentation> presentations) {
		this.presentations = presentations;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", " + firstName + " " + lastName + "]";
	}

}
