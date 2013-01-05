package Interface;

import Controller.SendNewMsg;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class INewTweet extends Parent {

  public Button send;
  TextField text = new TextField("Text");
  TextField lat = new TextField("Lattitude");
  TextField lon = new TextField("Longitude");

  public INewTweet() {
    GridPane pane = new GridPane();

    send = new Button("Envoyer !");
    pane.add(text, 0, 0, 3, 1);
    pane.add(lat, 0, 1, 1, 1);
    pane.add(lon, 1, 1, 1, 1);
    pane.add(send, 2, 1, 1, 1);
    this.getChildren().add(pane);

    send.setOnAction(new SendNewMsg(this));
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
