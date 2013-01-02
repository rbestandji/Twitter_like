package Interface;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import share.model.Message;

public class IMessage extends Parent {

  private Long id;
  private String author;
  private String text;
  private String date;

  IMessage(Long id, String author, String text, String date) {
    this.author = author;
    this.text = text;
    this.id = id;
    this.date = date;
    init();

  }

  private void init() {
    Label lauthor = new Label();
    lauthor.setText(author);
    Label ldate = new Label();
    ldate.setText(date);
    Label ltext = new Label();
    ltext.setText(text);

    GridPane gridpane = new GridPane();
    gridpane.add(lauthor, 0, 0, 1, 1);
    gridpane.add(ldate, 1, 0, 1, 1);
    gridpane.add(ltext, 0, 1, 2, 1);


    gridpane.setHgap(10.);
    gridpane.setVgap(10.);

    this.getChildren().add(gridpane);
  }
}
