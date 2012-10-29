package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "Message")
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  private Long id;
  @ManyToOne
  private User auteur = new User();
  @Column( length = 500)
  private String text = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date date_envoie = new Date();
  @Column
  private Long estUnCommentaire = null;
  
  @OneToMany
  private Collection<Message> commentaires = new ArrayList<Message>();

  public Message() {
  }

  public Message(String text, Date date) {
    setText(text);
    setDate(date);
  }

  /**
   * *************************************************
   * Getters et Setters *************************************************
   */
  public Long getEstUnCommentaire() {
    return estUnCommentaire;
  }

  public void setEstUnCommentaire(Long estUnCommentaire) {
    this.estUnCommentaire = estUnCommentaire;
  }

  public Collection<Message> getCommentaires() {
    return commentaires;
  }

  public void setCommentaires(Collection<Message> commentaires) {
    this.commentaires = commentaires;
  }

  public Date getDate_envoie() {
    return date_envoie;
  }

  public void setDate_envoie(Date date_envoie) {
    this.date_envoie = date_envoie;
  }

  public User getAuteur() {
    return auteur;
  }

  public void setAuteur(User auteur) {
    this.auteur = auteur;
  }

  public Long getId() {
    return id;
  }

  public Date getDate() {
    return date_envoie;
  }

  public void setDate(Date date) {
    this.date_envoie = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
