package Interface;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {

  private TextField emailTextField;
  private TextField mdpTextField;
  private PageProfil pageProfil;
  private Stage stage;
  
  LoginPage(PageProfil pageProfil) {
    this.pageProfil = pageProfil;
  }

  private void init(Stage stage) {
    Group root = new Group();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 400, 100));

    Label emailLabel = new Label("Email : ");
    emailTextField = new TextField();
    Label mdpLabel = new Label("Mot de passe : ");
    mdpTextField = new TextField();
    Button connexion = new Button("Connexion");
    connexion.setOnAction(new EventHandler<ActionEvent>() {
              public void handle(ActionEvent e) { connexion();} });
    
    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.add(emailLabel, 0, 0);
    grid.add(emailTextField, 1, 0);
    grid.add(mdpLabel, 0, 1);
    grid.add(mdpTextField, 1, 1);
    grid.add(connexion, 0, 2);

    root.getChildren().add(grid);
  }

  public void start(Stage stage) {
    this.stage=stage;
    init(stage);
    stage.show();
  }
  
  private void connexion() {
      stage.hide();
      stage = new Stage();
      pageProfil.start(stage);
  }
}
