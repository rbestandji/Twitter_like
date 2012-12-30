package core;

import share.core.Status;
import share.core.DAOExceptionUser;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction; 
import share.model.Message;
import share.model.User;

public class CommentDAO {
  /*
   * Permet à l'utilisateur connecté de commenter un message
   */

  public static void sendComment(Long idUser, Long idMessage, Message comment) throws DAOExceptionUser {

    UserTransaction utx = null;
    User uTmp;
    boolean problemAuthorId = false;
    boolean problemMessageId = false;

    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      uTmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (uTmp != null) {
        comment.setAuthor(uTmp);
        Message m = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + idMessage + "").getSingleResult();
        if (m != null) {
          comment.setMsgRoot(m); 
          m.addComment(comment); 
          em.persist(comment);
        } else { 
          problemMessageId = true;
        }
      } else {
        problemAuthorId = true;
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
      throw new DAOExceptionUser(new Status(Status.DB_ERROR), ex.getMessage());
    }

    if (problemAuthorId) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    } else if (problemMessageId) {
      throw new DAOExceptionUser(new Status(Status.NO_MESSAGE_ID));
    }
  }
  
  
}
