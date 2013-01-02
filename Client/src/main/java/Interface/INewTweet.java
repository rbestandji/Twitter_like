package Interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class INewTweet extends Parent {

  public Button send;
  
  public INewTweet() {
    GridPane pane = new GridPane();
    TextField text = new TextField("Text");
    TextField lat = new TextField("Lattitude");
    TextField lon = new TextField("Longitude");
    send = new Button("Envoyer !");
    pane.add(text, 0, 0, 3, 1);
    pane.add(lat, 0, 1, 1, 1);
    pane.add(lon, 1, 1, 1, 1);
    pane.add(send, 2, 1, 1, 1);
    this.getChildren().add(pane);
  }
}
