package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tGroup")
public class Group implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  @Column(name = "GROUP_ID")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User creator = null;
  
  @Column( length = 500)
  private String name = "";

  public Group() {
  }

  public Group(String name) {
    this.name = name;
  }

  public Group(String name, User creator) {
    this.name = name;
    this.creator = creator;
  }

  /**
   * *************************************************
   * Getters et Setters ************************************************
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
