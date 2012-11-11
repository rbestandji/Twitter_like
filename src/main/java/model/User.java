
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

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
  @Column( length = 50)
  private String password = "";
  @Column( length = 100)
  private String email = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date registration_date = new Date();
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date last_login_date = new Date();
  @OneToMany
  private List<Message> messages = new ArrayList<Message>();
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
  private List<Group> groups = new ArrayList<Group>();

  /* Constructeurs */
  public User() {
  }

  public User(String name, String firstname) {
    this(name, firstname, "", "");
  }

  public User(String name, String firstname, String email, String password) {
    this.setName(name);
    this.setFirstname(firstname);
    this.setPassword(password);
    this.setEmail(email);
  }

  /**
   * *************************************************
   * Getters et Setters ************************************************
   */
  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setRegistration_date(Date registration_date) {
    this.registration_date = registration_date;
  }

  public void setLast_login_date(Date last_login_date) {
    this.last_login_date = last_login_date;
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

  public Date getRegistration_date() {
    return registration_date;
  }

  public Date getLast_login_date() {
    return last_login_date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void removeRecursionProblem() {
    for (int i = 0; i < this.groups.size(); i++) {
      this.groups.get(i).setCreator(null);
    }
  }
}
