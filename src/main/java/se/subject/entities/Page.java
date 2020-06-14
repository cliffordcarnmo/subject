package se.subject.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"page\"")
@NamedQuery(name = "Page.findAll", query = "SELECT p FROM Page p")
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private long pageid;

	@Column(nullable = false)
	private Boolean active;

	@Lob
	private String content;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp created;

	@Column(nullable = false, length = 1024)
	private String name;

	private long parentid;

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

	public long getPageid() {
		return this.pageid;
	}

	public void setPageid(long pageid) {
		this.pageid = pageid;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Attachment addAttachment(Attachment attachment) {
		getAttachments().add(attachment);
		attachment.setPage(this);

		return attachment;
	}

	public Attachment removeAttachment(Attachment attachment) {
		getAttachments().remove(attachment);
		attachment.setPage(null);

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
		comment.setPage(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setPage(null);

		return comment;
	}

	public Space getSpace() {
		return this.space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}