package Interface;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;




public class PageProfil extends Application{
  
  public PageProfil(){
    
  }
  
  @Override
  public void start(Stage stage){
     Group root = new Group();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 400, 100));

    Label emailLabel = new Label("Email : ");
    Label mdpLabel = new Label("Mot de passe : ");
    Button connexion = new Button("Connexion");

    
    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.add(emailLabel, 0, 0);
    grid.add(mdpLabel, 0, 1);
    grid.add(connexion, 0, 2);

    root.getChildren().add(grid);
    stage.show();
  }
  
}
