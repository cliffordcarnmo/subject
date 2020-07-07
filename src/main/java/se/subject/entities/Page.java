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
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"PAGE\"")
@NamedQuery(name = "Page.findAll", query = "SELECT p FROM Page p")
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int pageid;

	@Lob
	private String content;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	@Column(nullable = false, length = 1024)
	private String name;

	@Column(unique = true, nullable = false, length = 1024)
	private String url;
	
	//private Page parentid;

    @ManyToOne
    @JoinColumn(name = "parentid")
    private Page parent;

    @OneToMany(mappedBy = "parent")
    private List<Page> children;
   
	@UpdateTimestamp
	private Timestamp updated;

	// bi-directional many-to-one association to Attachment
	@OneToMany(mappedBy = "page")
	private List<Attachment> attachments;

	// bi-directional many-to-one association to Comment
	@OneToMany(mappedBy = "page")
	private List<Comment> comments;

	// bi-directional many-to-one association to Space
	@ManyToOne
	@JoinColumn(name = "SPACEID", nullable = false)
	private Space space;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "USERID", nullable = false)
	private User user;

    public Page() {
	}
    
    public Attachment addAttachment(Attachment attachment) {
		getAttachments().add(attachment);
		attachment.setPage(this);
		return attachment;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setPage(this);
		return comment;
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public List<Page> getChildren() {
        return children;
    }

	public List<Comment> getComments() {
		return this.comments;
	}

	public String getContent() {
		return this.content;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public String getName() {
		return this.name;
	}

	public int getPageid() {
		return this.pageid;
	}

	public Page getParentid() {
		return this.parent;
	}
	
	public void setParentid(Page page) {
		this.parent = page;
	}

	public String getPermalink() {
		return "https://www.subject.se/space/" + this.getSpace().getUrl() + "/" + this.getUrl();
	}

	public Space getSpace() {
		return this.space;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public String getUrl() {
		return this.url;
	}
    
	public User getUser() {
		return this.user;
	}

	public Attachment removeAttachment(Attachment attachment) {
		getAttachments().remove(attachment);
		attachment.setPage(null);
		return attachment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setPage(null);
		return comment;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPageid(int pageid) {
		this.pageid = pageid;
	}

	public void setParent(Page parent) {
		this.parent = parent;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(User user) {
		this.user = user;
	}
}