/*
    Subject
    Copyright (C) 2020 Clifford Carnmo

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package se.subject.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="\"SPACE\"")
@NamedQuery(name="Space.findAll", query="SELECT s FROM Space s")
public class Space implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int spaceid;

	@Column(nullable=false)
	@CreationTimestamp
	private Timestamp created;

	@Column(length=1024)
	private String description;

	@Column(nullable=false, length=1024)
	private String name;
	
	@Column(nullable = false, length = 1024)
	private String url;

	@UpdateTimestamp
	private Timestamp updated;

	// bi-directional many-to-one association to Page
	@OneToMany(mappedBy = "space")
	private List<Page> pages;

	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
		name="\"SPACEUSER\""
		, joinColumns=@JoinColumn(name="SPACEID", nullable=false)
		, inverseJoinColumns={
			@JoinColumn(name="USERID", nullable=false)
			}
		)
	private List<User> users;

	public Space() {
	}

	public Page addPage(Page page) {
		getPages().add(page);
		page.setSpace(this);
		return page;
	}

	public Timestamp getCreated() {
		return this.created;
	}
	
	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public Boolean getOperator(User user) {
		for (User u : getUsers()) {
			if (u.getUserid() == user.getUserid()) {
				return true;
			}
		}
		return false;
	}

	public List<Page> getPages() {
		return this.pages;
	}

	public String getPermalink() {
		return "https://www.subject.se/space/" + this.getUrl() + "/";
	}

	public int getSpaceid() {
		return this.spaceid;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}
	
	public String getUrl() {
		return this.url;
	}

	public List<User> getUsers() {
		return this.users;
	}
	
	public Page removePage(Page page) {
		getPages().remove(page);
		page.setSpace(null);
		return page;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public void setSpaceid(int spaceid) {
		this.spaceid = spaceid;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}