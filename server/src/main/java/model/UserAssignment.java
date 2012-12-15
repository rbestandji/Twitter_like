package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
public class UserAssignment implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  private User follower = new User();
  @ManyToOne
  private User following = new User();

  public UserAssignment() {
  }

  public UserAssignment(User follower, User following) {
    this.follower = follower;
    this.following = following;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonBackReference("er")
  public User getFollowing() {
    return following;
  }

  public void setFollowing(User following) {
    this.following = following;
  }

  @JsonBackReference("ing")
  public User getFollower() {
    return follower;
  }

  public void setFollower(User follower) {
    this.follower = follower;
  }
}
