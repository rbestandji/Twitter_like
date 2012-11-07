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
@Table(name = "Groupe")
public class Groupe implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  @Column(name = "GROUPE_ID")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User createur = null;
  
  @Column( length = 500)
  private String nom = "";

  public Groupe() {
  }

  public Groupe(String nom) {
    this.nom = nom;
  }

  public Groupe(String nom, User createur) {
    this.nom = nom;
    this.createur = createur;
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

  public User getCreateur() {
    return createur;
  }

  public void setCreateur(User createur) {
    this.createur = createur;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}
