package Interface;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import share.model.User;

public class IUser extends Group {

  private User user;
  private Text name;
  private Text surname;
  private Text email;

  public IUser() {
    super();
    this.user = null;
    init();
  }

  public void setUser(User user) {
    this.user = (user);
    this.name.setText(user.getFirstname());
    this.surname.setText(user.getName());
    this.email.setText(user.getEmail());
  }

  private void init() {
    int W = 10;
    int H = 10;

    // Scene root
    GridPane grid = new GridPane();
    grid.setHgap(W);
    grid.setVgap(H);

    // Scene
    name = new Text("Bob");
    surname= new Text("Bob");
    email= new Text("email");

    
    // Scene tree
    grid.add(name, 0, 0, 5, 1);
    grid.add(surname, 5, 0, 5, 1);
    grid.add(email, 0, 1, 10, 1);


    this.getChildren().add(grid);
  }
}
