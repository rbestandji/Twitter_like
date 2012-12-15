package model;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
@Table(name = "tUser")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "USER_ID")
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column( length = 50)
  private String name = "";
  @Column( length = 50)
  private String firstname = "";
  @Column( length = 40)
  private String password = "";
  @Column( length = 100)
  private String email = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date registrationDate = new Date();
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date lastLoginDate = new Date();
  @OneToMany
  @JsonManagedReference("msg_e")
  private List<Message> messages = new ArrayList<Message>();
  @OneToMany( mappedBy = "following")
  @JsonManagedReference("ing")
  private List<UserAssignment> usersFollowing = new ArrayList<UserAssignment>();
  @OneToMany( mappedBy = "follower")
  @JsonManagedReference("er")
  private List<UserAssignment> usersFollowers = new ArrayList<UserAssignment>();

  // Constructeurs
  public User() {
  }

  public User(String name, String firstname) throws DAOExceptionUser, NoSuchAlgorithmException {
    this(name, firstname, "", "");
  }

  public User(String name, String firstname, String email, String password)
          throws DAOExceptionUser , NoSuchAlgorithmException {
    try {
      this.setPassword(password);
      this.setEmail(email);
    } catch (DAOExceptionUser ex) {
      throw ex;
    }
    this.setName(name);
    this.setFirstname(firstname);
  }

  public List<UserAssignment> getUsersFollowing() {
    return usersFollowing;
  }

  public void setUsersFollowing(List<UserAssignment> usersFollowing) {
    this.usersFollowing = usersFollowing;
  }

  public List<UserAssignment> getUsersFollowers() {
    return usersFollowers;
  }

  public void setUsersFollowers(List<UserAssignment> usersFollowers) {
    this.usersFollowers = usersFollowers;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) throws DAOExceptionUser, NoSuchAlgorithmException{
    if (password.length() < 8) {
      throw new DAOExceptionUser(new Status(Status.PASSWORD_TOO_SHORT));
    } else {
      this.password = UserDAO.sha1sum(password);
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setEmail(String email) throws DAOExceptionUser {
    int isValid = 0;
    int i = 0;
    char c;
    while (i < email.length()) {
      c = email.charAt(i);
      if (Character.isSpaceChar(c)) {
        isValid = 0;
        break;
      }
      if (c == '@') {
        isValid++;
      }
      i++;
    }
    if (isValid == 1) {
      this.email = email;
    } else {
      throw new DAOExceptionUser(new Status(Status.INVALID_EMAIL));
    }
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  public void setLastLoginDate(Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public String getName() {
    return name;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getEmail() {
    return email;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public Date getLastLoginDate() {
    return lastLoginDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
