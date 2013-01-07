package share.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
public class Message extends Communication implements Serializable {

  @OneToMany(mappedBy = "msgRoot", cascade = CascadeType.ALL)
  @JsonManagedReference("com")
  private List<Message> comments = new ArrayList<Message>();
  @ManyToOne
  @JsonBackReference("com")
  private Message msgRoot = null;

  public Message() {
  }

  public Message(String text, Date msgDate) {
    super(text, msgDate);
  }

  public List<Message> getComments() {
    return comments;
  }

  public void setComments(List<Message> comments) {
    this.comments = comments;
  }

  public void addComment(Message comment) {
    this.comments.add(comment);
  }

  public void removeComment(Message comment) {
    this.comments.remove(comment);
  }

  public Message getMsgRoot() {
    return msgRoot;
  }

  public void setMsgRoot(Message msgRoot) {
    this.msgRoot = msgRoot;
  }
}
