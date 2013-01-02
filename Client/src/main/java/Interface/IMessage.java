package Interface;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import share.model.Message;

public class IMessage extends Parent {

  private Message msg;

  public IMessage(Message msg) {
    this.msg = msg;
    init();
  }

  private void init() {
    Label auteur = new Label();
    auteur.setText(msg.getAuthor().getName());
    Label date = new Label();
    date.setText(msg.getMsgDate().toString());
    Label text = new Label();
    text.setText(msg.getText());

    GridPane gridpane = new GridPane();
    gridpane.add(auteur, 0, 0, 1, 1);
    gridpane.add(auteur, 1, 0, 1, 1);
    gridpane.add(auteur, 0, 1, 2, 1);


    gridpane.setHgap(10.);
    gridpane.setVgap(10.);

    this.getChildren().add(gridpane);
  }
}
