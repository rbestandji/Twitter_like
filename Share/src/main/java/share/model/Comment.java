package share.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
public class Comment extends Communication implements Serializable {

  @ManyToOne
  @JsonBackReference("com")
  private Message msgRoot = new Message();

  public Comment() {
    super();
    super.setIsComment(true);
  }

  public Comment(String text, Date msgDate) {
    super(text, msgDate);
    super.setIsComment(true);
  }
    
  public Comment(String text, Date msgDate, Message msgRoot) {
    super(text, msgDate);
    super.setIsComment(true);
    setMsgRoot(msgRoot);
  }
    
  public Message getMsgRoot() {
    return msgRoot;
  }
  
  public void setMsgRoot(Message msgRoot) {
    this.msgRoot=msgRoot;
  }
}
