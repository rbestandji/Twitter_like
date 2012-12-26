package share.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
public class Message extends Communication implements Serializable {

  @OneToMany(mappedBy="msgRoot", cascade = CascadeType.ALL)
  @JsonManagedReference("com")
  private Collection<Comment> comments = new ArrayList<>();

  public Message() {}

  public Message(String text, Date msgDate) {
    super(text, msgDate);   
  }
    
  public Collection<Comment> getComments() {
    return comments;
  }
  public void setComments(Collection<Comment> comments) {
    this.comments=comments;
  }
  
  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
  }

}
