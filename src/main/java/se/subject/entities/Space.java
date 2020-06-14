package se.subject.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"space\"")
@NamedQuery(name = "Space.findAll", query = "SELECT s FROM Space s")
public class Space implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private long spaceid;

	@Column(nullable = false)
	private Boolean active;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	@Column(nullable = false, length = 1024)
	private String name;

	@Column(nullable = true, length = 1024)
	private String description;

	@UpdateTimestamp
	private Timestamp updated;

	@Column(nullable = false)
	private long userid;

	// bi-directional many-to-one association to Page
	@OneToMany(mappedBy = "space")
	private List<Page> pages;

	// bi-directional many-to-many association to User
	@ManyToMany(mappedBy = "spaces")
	private List<User> users;

	public Space() {
	}

	public long getSpaceid() {
		return this.spaceid;
	}

	public void setSpaceid(long spaceid) {
		this.spaceid = spaceid;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public List<Page> getPages() {
		return this.pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public Page addPage(Page page) {
		getPages().add(page);
		page.setSpace(this);

		return page;
	}

	public Page removePage(Page page) {
		getPages().remove(page);
		page.setSpace(null);

		return page;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}