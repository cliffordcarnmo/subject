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
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"comment\"")
@NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private long commentid;

	@Column(nullable = false)
	private Boolean active;

	@Lob
	private String content;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	private long parentid;

	@UpdateTimestamp
	private Timestamp updated;

	// bi-directional many-to-one association to Page
	@ManyToOne
	@JoinColumn(name = "PAGEID", nullable = false)
	private Page page;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "USERID", nullable = false)
	private User user;

	public Comment() {
	}

	public long getCommentid() {
		return this.commentid;
	}

	public void setCommentid(long commentid) {
		this.commentid = commentid;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public long getParentid() {
		return this.parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Page getPage() {
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}