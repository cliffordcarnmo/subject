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
@Table(name = "\"attachment\"")
@NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a")
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int attachmentid;

	@Lob
	private String content;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	@Column(nullable = false, length = 1024)
	private String name;

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

	public Attachment() {
	}

	public int getAttachmentid() {
		return this.attachmentid;
	}

	public void setAttachmentid(int attachmentid) {
		this.attachmentid = attachmentid;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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