package share.core;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class Status implements StatusType {

  public final static int USER_OFFLINE = 0;//utilisateur non connecté
  public final static int USER_ONLINE = 1;//utilisateur connecté
  public final static int USER_NO_ACCOUNT = 2;//L'utilisateur n'a pas de compte
  public final static int USER_WRONG_PASSWORD = 3;//mauvais mot de passe utilisateur
  public final static int DB_ERROR = 4;// problème d'accès à la base de données
  public final static int OK = 200;
  public final static int EMAIL_VALIDATED = 5;//validation de l'email
  public final static int NO_MESSAGE_ID = 6;//pas d'identifiant pour le message
  public final static int WRONG_USER = 7;//l'utilisateur n'est pas celui attendu
  public final static int PASSWORD_TOO_SHORT = 8;//8 caractères minimum
  public final static int INVALID_EMAIL = 9;//l'email doit contenir un @ et pas d'espace
  public final static int ID_NOT_EXIST = 10;//L'identifiant n'existe pas
  public final static int NOT_FOLLOWING = 11;//Pas de relation following
  public final static int EMPTY_FIELD = 12;//champs vide

  private int code;

  public Status(int code) {
    this.code = code;
  }

  @Override
  public int getStatusCode() {
    return code;
  }

  @Override
  public Family getFamily() {
    return Family.OTHER;
  }

  @Override
  public String getReasonPhrase() {
    String str = "";
    switch (code) {
      case USER_OFFLINE: str = "Veuillez vous connecter pour avoir accès à cette fonctionnalité"; break;
      case USER_ONLINE: str = "Vous êtes déjà connecté avec un autre compte"; break;
      case USER_NO_ACCOUNT: str = "L'email est incorrect"; break;
      case USER_WRONG_PASSWORD: str = "Le mot de passe est incorrect"; break;
      case DB_ERROR: str = "Une erreur s'est produite dans la base de données"; break;
      case EMAIL_VALIDATED: str = "Cet email est déjà pris"; break;
      case NO_MESSAGE_ID: str = "Ce message n'existe pas dans la base de données"; break;
      case WRONG_USER: str = "Droits insuffisants pour effectuer cette opération"; break;
      case PASSWORD_TOO_SHORT: str = "Le mot de passe doit contenir\n au moins 8 caractères"; break;
      case INVALID_EMAIL: str = "L'email n'est pas valide"; break;
      case ID_NOT_EXIST: str = "Pas de résultat trouvé dans la base de données"; break;
      case NOT_FOLLOWING: str = "Vous n'êtes pas abonnés à cet utilisateur"; break;
      case EMPTY_FIELD: str = "Nom ou Prénom non renseigné"; break;
      default: str = "Erreur inconnue";
    }
    return str;
  }
  
}
