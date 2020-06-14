package se.subject.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"user\"")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private long userid;

	@Column(nullable = false)
	private Boolean active;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	@Column(nullable = false, length = 1024)
	private String email;

	@Column(nullable = false, length = 1024)
	private String name;

	@Column(nullable = false, length = 88)
	private String salt;

	@Column(nullable = false, length = 172)
	private String hash;

	@UpdateTimestamp
	private Timestamp updated;

	// bi-directional many-to-one association to Attachment
	@OneToMany(mappedBy = "user")
	private List<Attachment> attachments;

	// bi-directional many-to-one association to Comment
	@OneToMany(mappedBy = "user")
	private List<Comment> comments;

	// bi-directional many-to-one association to Page
	@OneToMany(mappedBy = "user")
	private List<Page> pages;

	// bi-directional many-to-many association to Space
	@ManyToMany
	@JoinTable(name = "\"spaceuser\"", joinColumns = {
			@JoinColumn(name = "USERID", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "SPACEID", nullable = false) })
	private List<Space> spaces;
	
	public User() {
	}
	
	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Attachment addAttachment(Attachment attachment) {
		getAttachments().add(attachment);
		attachment.setUser(this);

		return attachment;
	}

	public Attachment removeAttachment(Attachment attachment) {
		getAttachments().remove(attachment);
		attachment.setUser(null);

		return attachment;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	public List<Page> getPages() {
		return this.pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public Page addPage(Page page) {
		getPages().add(page);
		page.setUser(this);

		return page;
	}

	public Page removePage(Page page) {
		getPages().remove(page);
		page.setUser(null);

		return page;
	}

	public List<Space> getSpaces() {
		return this.spaces;
	}

	public void setSpaces(List<Space> spaces) {
		this.spaces = spaces;
	}
}