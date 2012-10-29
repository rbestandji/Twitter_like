package model;

import java.io.Serializable;
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

@Entity
@Table(name = "Utilisateur")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column( length = 50)
  private String nom = "";
  @Column( length = 50)
  private String prenom = "";
  @Column( length = 50)
  private String mdp = "";
  @Column( length = 100)
  private String email = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date date_inscription = new Date();
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date date_derniere_connection = new Date();
  @OneToMany
  private List<Message> listeMessage = new ArrayList<Message>();

  /* Constructeurs */
  public User() {
  }

  public User(String nom, String prenom) {
    this(nom, prenom, "", "");
  }

  public User(String nom, String prenom, String email, String mdp) {
    this.setNom(nom);
    this.setPrenom(prenom);
    this.setMdp(mdp);
    this.setEmail(email);
  }


  /**
   * *************************************************
   * Getters et Setters 
   **************************************************
   */
  public List<Message> getListeMessage() {
    return listeMessage;
  }

  public void setListeMessage(List<Message> listeMessage) {
    this.listeMessage = listeMessage;
  }

  public String getMdp() {
    return mdp;
  }

  public void setMdp(String mdp) {
    this.mdp = mdp;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setDate_inscription(Date date_inscription) {
    this.date_inscription = date_inscription;
  }

  public void setDate_derniere_connection(Date date_derniere_connection) {
    this.date_derniere_connection = date_derniere_connection;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public String getEmail() {
    return email;
  }

  public Date getDate_inscription() {
    return date_inscription;
  }

  public Date getDate_derniere_connection() {
    return date_derniere_connection;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
