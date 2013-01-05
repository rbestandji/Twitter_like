package share.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "tCommunication")
public class Communication implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.SEQUENCE)
  private Long id;
  @ManyToOne
  @JsonBackReference("msg_e")
  private User author = new User();
  @Column( length = 500)
  private String text = "";
  @Temporal( javax.persistence.TemporalType.DATE)
  private Date msgDate;
  private double latitude = -500;
  private double longitude = -500;

  public Communication() {
  }

  public Communication(String text, Date msgDate) {
    setText(text);
    setMsgDate(msgDate);
  }

  public Date getMsgDate() {
    return msgDate;
  }

  public void setMsgDate(Date msgDate) {
    this.msgDate = msgDate;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
