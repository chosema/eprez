package sk.tuke.kpi.core.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements HasId<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 50)
	private String login;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@OneToMany(mappedBy = "user")
	private List<Presentation> presentations;

	public User() { // default constructor
	}

	public User(final String login, final String firstName, final String lastName) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
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

	public List<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(final List<Presentation> presentations) {
		this.presentations = presentations;
	}

}
