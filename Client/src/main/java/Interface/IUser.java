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
import share.model.User;

public class IUser extends Parent {

  private User user;
  private Label gridCaption;
  private GridPane grid;
  private Button validate;
  private Button returnB;
  private GetUserTask post = new GetUserTask();
  private TextField email = new TextField();
  private TextField name = new TextField();
  private TextField firstname = new TextField();
  private TextField password = new PasswordField();

  /*
  public void setUser(User user) {
    this.user = (user);
    this.name.setText(user.getFirstname());
    this.surname.setText(user.getName());
    this.email.setText(user.getEmail());
  }*/

  public IUser() throws URISyntaxException, MalformedURLException {
    gridCaption = new Label("Inscription");
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
    
    label = new Label("Nom:");
    GridPane.setConstraints(label,0,2);
    GridPane.setHalignment(label,HPos.RIGHT);
    grid.getChildren().add(label);

    GridPane.setConstraints(name,1,2);
    GridPane.setHalignment(name,HPos.LEFT);
    grid.getChildren().add(name);
    
    label = new Label("Prénom:");
    GridPane.setConstraints(label,0,3);
    GridPane.setHalignment(label,HPos.RIGHT);
    grid.getChildren().add(label);

    GridPane.setConstraints(firstname,1,3);
    GridPane.setHalignment(firstname,HPos.LEFT);
    grid.getChildren().add(firstname);
    
    // bouton valider
    validate = new Button("Valider");
    validate.setTranslateX(120);
    validate.setTranslateY(160);

    // bouton retour
    returnB = new Button("Retour");
    returnB.setTranslateX(400);
    returnB.setTranslateY(450);

    this.getChildren().add(gridCaption);
    this.getChildren().add(grid);
    this.getChildren().add(validate);
    this.getChildren().add(returnB);
    
    // click bouton retour
    returnB.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          getChildren().clear();
          CUser cuser = new CUser();
          getChildren().add(cuser);
        } catch (URISyntaxException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
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
          f.add("name", name.getText());
          f.add("firstname", firstname.getText());
          ClientResponse result = post.postCall("registration",f);
          result.close();
          getChildren().clear();
          CUser cuser = new CUser();
          getChildren().add(cuser);
        } catch (MalformedURLException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
  }
}

