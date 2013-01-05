package Interface;

import Controller.SendNewComment;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class INewComment extends Parent {

  public Button send;
  private TextField text = new TextField("Text");
  private TextField lat = new TextField("Lattitude");
  private TextField lon = new TextField("Longitude");
  private Long id;
  private Stage stage;
  
  public INewComment(Long id, Stage stage) {
    this.id = id;
    this.stage = stage;
    GridPane pane = new GridPane();

    send = new Button("Envoyer !");
    pane.add(text, 0, 0, 3, 1);
    pane.add(lat, 0, 1, 1, 1);
    pane.add(lon, 1, 1, 1, 1);
    pane.add(send, 2, 1, 1, 1);
    this.getChildren().add(pane);

    send.setOnAction(new SendNewComment(this));
  }
  
  public Stage getStage(){
    return stage;
  }
  
 public Long getIdMsg(){
    return id;
  }
 
  public String getText() {
    return text.getText();
  }

  public Double getLongitude() {
    Double x = null;
    try {
      x = Double.parseDouble(lon.getText());
    } catch (Exception ex) {
      x = new Double(-500);
    };
    return x;
  }

  public Double getLatitude() {
    Double x = null;
    try {
      x = Double.parseDouble(lat.getText());
    } catch (Exception ex) {
      x = new Double(-500);
    };
    return x;
  }
}
