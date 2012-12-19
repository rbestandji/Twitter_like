package share.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "tMessage")
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  private Long id;
  @ManyToOne
  @JsonBackReference("msg_e")
  private User author = new User();
  @Column( length = 500)
  private String text = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date msgDate;
  @Column
  private Long isComment = null;
  @OneToMany(cascade = CascadeType.ALL)
  private Collection<Message> comments = new ArrayList<Message>();

  public Message() {
  }

  public Message(String text, Date msgDate) {
    setText(text);
    setMsgDate(msgDate);
  }

  /**
   * *************************************************
   * Getters et Setters *************************************************
   */
  public Long getIsComment() {
    return isComment;
  }

  public void setIsComment(Long isComment) {
    this.isComment = isComment;
  }

  public Collection<Message> getComments() {
    return comments;
  }

  public void setComments(Collection<Message> comments) {
    this.comments = comments;
  }

  public Date getMsgDate() {
    return msgDate;
  }

  public void setMsgDate(Date msgDate) {
    this.msgDate = msgDate;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
