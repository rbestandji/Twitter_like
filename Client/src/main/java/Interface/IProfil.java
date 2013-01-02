package Interface;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import share.model.User;

public class IProfil extends Parent {

  private User user;

  public IProfil() {
  }

  public void setUser(User user) {
    this.user = user;
    this.getChildren().clear();
    init();
  }

  private void init() {
    GridPane pane = new GridPane();
    pane.add(new Label(user.getName()), 0, 0, 1, 1);
    pane.add(new Label(user.getFirstname()), 1, 0, 1, 1);
    pane.add(new Label(user.getEmail()), 0, 1, 2, 1);
  }
}
