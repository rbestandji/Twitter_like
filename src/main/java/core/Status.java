package core;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class Status implements StatusType {

  public final static int USER_NOT_LOGGED_IN = 0;//utilisateur non connecté
  public final static int USER_LOGGED_IN = 1;//utilisateur connecté
  public final static int USER_NO_ACCOUNT = 2;//L'utilisateur n'a pas de compte
  public final static int USER_WRONG_PASSWORD = 3;//mauvais mot de passe utilisateur
  public final static int DB_ERROR = 4;// problème d'accès à la base de données
  public final static int OK = 200;
  public final static int EMAIL_VALIDATED = 5;//validation de l'email
  public final static int NO_MESSAGE_ID = 6;//pas d'identifiant pour le message
  public final static int WRONG_USER = 7;//l'utilisateur n'est pas celui attendu
  private int code;

  public Status(int code) {
    this.code = code;
  }

  public int getStatusCode() {
    return code;
  }

  public Family getFamily() {
    return Family.OTHER;
  }

  public String getReasonPhrase() {
    String str = "";
    switch (code) {
      /* Pff long à faire pour rien ....
       * Seriously !!!
       */
    }
    return str;
  }
}
