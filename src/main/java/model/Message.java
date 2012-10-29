package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
  //@JoinColumn(name="Utilisateur_id")
  @Column( length = 500)
  private String text = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date date_envoie = new Date();

  public Message() {
  }

  public Message(String text, Date date) {
    setText(text);
    setDate(date);
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
