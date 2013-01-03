package Interface;

import Controller.ConnectionOtherUser;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import share.model.User;

public class IMessage extends Parent {

  private Long id;
  private User author;
  private String text;
  private String date;

  IMessage(Long id, User author, String text, String date) {
    this.author = author;
    this.text = text;
    this.id = id;
    this.date = date;
    init();
  }

  private void init() {
    Button lauthor = new Button();
    lauthor.setText(author.getName());
    Label ldate = new Label();
    ldate.setText(date);
    Label ltext = new Label();
    ltext.setText(text);

    ldate.setTextFill(Color.GREEN);
    ltext.setWrapText(true);
    lauthor.setWrapText(false);
    ltext.setMinSize(390, 30);

    GridPane gridpane = new GridPane();
    gridpane.getColumnConstraints().add(new ColumnConstraints(200));
    gridpane.getColumnConstraints().add(new ColumnConstraints(200));

    gridpane.setAlignment(Pos.BASELINE_LEFT);
    gridpane.add(lauthor, 0, 0, 1, 1);
    gridpane.add(ldate, 1, 0, 1, 1);
    gridpane.add(ltext, 0, 1, 2, 1);

    gridpane.setHgap(10.);
    gridpane.setVgap(10.);

    gridpane.setMaxWidth(400);
    gridpane.setMinWidth(400);

    this.getChildren().add(gridpane);

    lauthor.setOnAction(new ConnectionOtherUser(author.getId(), null));
  }
}
