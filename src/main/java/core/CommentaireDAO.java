package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.User;
import model.Message;

public class CommentaireDAO {
  /*
   * Permet à l'utilisateur connecté de commenter un message
   */

  public static void envoieCommentaire(Long idUser, Long idMessage, Message commentaire) throws DAOExceptionUser {

    UserTransaction utx = null;
    User u_tmp = null;
    boolean problemIdAuteur = false;
    boolean problemIdMessage = false;

    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (u_tmp != null) {
        commentaire.setAuteur(u_tmp);
        Message m = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + idMessage + "").getSingleResult();
        if (m != null) {
          Collection<Message> liste = m.getCommentaires();
          liste.add(commentaire);
          m.setCommentaires(liste);
          commentaire.setEstUnCommentaire(m.getId());
          em.persist(commentaire);
        } else {
          problemIdMessage = true;
        }
      } else {
        problemIdAuteur = true;
      }
      utx.commit();

    } catch (Exception ex) {
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }

    if (problemIdAuteur) {
      throw new DAOExceptionUser(new Status(Status.UTILISATEUR_PAS_DE_COMPTE));
    } else if (problemIdMessage) {
      throw new DAOExceptionUser(new Status(Status.PAS_ID_MESSAGE));
    }


  }
}
