package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CUser extends Parent {
  private Label gridCaption;
  private GridPane grid;
  private Button validate;
  private Button registration;
  private GetUserTask post = new GetUserTask();
  private TextField email = new TextField();
  private TextField password = new PasswordField();

  public CUser() throws URISyntaxException, MalformedURLException {
    gridCaption = new Label("Connexion");
    gridCaption.setWrapText(true);
    //this.setTranslateX(10);
    //this.setTranslateY(10);

    grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(10);
    grid.setPadding(new Insets(18));
    
    Label label = new Label("Email:");
    GridPane.setConstraints(label,0,0);
    GridPane.setHalignment(label,HPos.RIGHT);
    grid.getChildren().add(label);

    GridPane.setConstraints(email,1,0);
    GridPane.setHalignment(email,HPos.LEFT);
    grid.getChildren().add(email);
    
    label = new Label("Mot de passe:");
    GridPane.setConstraints(label,0,1);
    GridPane.setHalignment(label,HPos.RIGHT);
    grid.getChildren().add(label);

    GridPane.setConstraints(password,1,1);
    GridPane.setHalignment(password,HPos.LEFT);
    grid.getChildren().add(password);
    
    // bouton valider
    validate = new Button("Valider");
    validate.setTranslateX(120);
    validate.setTranslateY(80);

    // bouton s'inscrire
    registration = new Button("S'inscrire");
    registration.setTranslateX(400);
    registration.setTranslateY(450);

    this.getChildren().add(gridCaption);
    this.getChildren().add(grid);
    this.getChildren().add(validate);
    this.getChildren().add(registration);
    
    // click bouton s'inscrire 
    registration.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          getChildren().clear();
          IUser iuser = new IUser();
          getChildren().add(iuser);
        } catch (URISyntaxException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    // click bouton valider 
    validate.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          Form f = new Form();
          f.add("email", email.getText());
          f.add("password", password.getText());
          ClientResponse result = post.postCall("connection",f);
          System.out.println(result.getEntity(String.class));
          result.close();
          getChildren().clear();
          Wall wall = new Wall();
          getChildren().add(wall);
        } catch (MalformedURLException ex) {
          Logger.getLogger(CUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
          Logger.getLogger(CUser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
  }
}

