package Interface;

import Controller.ConnectionOtherUser;
import Controller.DeleteMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import share.model.Message;
import share.model.User;

public class IMessage extends Parent {

  private Long id;
  private User author;
  private String text;
  private String date;
  private ArrayList<Message> listComments = null;

  IMessage(Long id, User author, String text, String date, ArrayList<Message> comments) {
    this.author = author;
    this.text = text;
    this.id = id;
    this.date = date;
    this.listComments = comments;
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
    gridpane.getColumnConstraints().add(new ColumnConstraints(250));
    gridpane.getColumnConstraints().add(new ColumnConstraints(200));
    gridpane.getColumnConstraints().add(new ColumnConstraints(30));

    gridpane.setAlignment(Pos.BASELINE_LEFT);
    gridpane.add(lauthor, 0, 0, 1, 1);
    gridpane.add(ldate, 1, 0, 1, 1);
    if (this.author.getId() == MainWindow.userConnected.getId()) {
      Button delete = new Button("X");
      delete.setTooltip(new Tooltip("Supprimer"));
      gridpane.add(delete, 2, 0, 1, 1);
      delete.setOnAction(new DeleteMessage(id));
    }
    gridpane.add(ltext, 0, 1, 3, 3);

    Button commenter = new Button("Commenter");
    commenter.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        Stage stage = new Stage();
        Group inside = new Group();
        stage.setScene(new Scene(inside, 430, 150));
        inside.getChildren().add(new INewComment(id, stage));
        stage.show();
      }
    });



    TitledPane comments = new TitledPane("Commentaires", functionComment(listComments));
    Accordion accordionComment = new Accordion();
    accordionComment.getPanes().add(comments);
    /*Rajout des commentaires */
    gridpane.add(commenter, 0, 4, 1, 1);
    gridpane.add(accordionComment, 0, 5, 3, 1);




    gridpane.setHgap(10.);
    gridpane.setVgap(10.);

    gridpane.setMaxWidth(400);
    gridpane.setMinWidth(400);

    this.getChildren().add(gridpane);

    lauthor.setOnAction(new ConnectionOtherUser(author.getId(), null));
  }

  private Group functionComment(ArrayList<Message> comments) {
    Group a = new Group();
    VBox b = new VBox();
    for (int i = 0; i < comments.size(); i++) {
      b.getChildren().add(new IMessage(comments.get(i).getId(), comments.get(i).getAuthor(), comments.get(i).getText(),
              new SimpleDateFormat("yyyy.MM.dd ' à ' HH:mm:ss").format(comments.get(i).getMsgDate()).toString(),
              (ArrayList)comments.get(i).getComments()));
    }
    a.getChildren().add(b);
    return a;
  }
}
