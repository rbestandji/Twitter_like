package core;

import java.util.Collection;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import model.User;
import model.Message;

public class CommentDAO {
  /*
   * Permet à l'utilisateur connecté de commenter un message
   */
  public static void sendComment(Long idUser, Long idMessage, Message comment) throws DAOExceptionUser {

    UserTransaction utx = null;
    User u_tmp = null;
    boolean problemIdAuthor = false;
    boolean problemIdMessage = false;

    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (u_tmp != null) {
        comment.setAuthor(u_tmp);
        Message m = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + idMessage + "").getSingleResult();
        if (m != null) {
          Collection<Message> list = m.getComments();
          list.add(comment);
          m.setComments(list);
          comment.setIsComment(m.getId());
          em.persist(comment);
        } else {
          problemIdMessage = true;
        }
      } else {
        problemIdAuthor = true;
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
      throw new DAOExceptionUser(new Status(Status.ERROR_DB), ex.getMessage());
    }

    if (problemIdAuthor) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    } else if (problemIdMessage) {
      throw new DAOExceptionUser(new Status(Status.NOT_ID_MESSAGE));
    }
  }
  
   /*
   * Permet à l'utilisateur connecté de supprimer un commentaire (Ne fonctionne pas encore!!!!)
   */
  public static void deleteComment(Long idUser, Long idMsg, Long idComment) throws DAOExceptionUser {

    UserTransaction utx = null;
    User u_tmp = null;
    boolean problemIdAuthor = false;
    boolean problemIdMessage = false;

    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (u_tmp != null) {
        //comment.setAuthor(u_tmp);
        em.createQuery("DELETE FROM Message x WHERE x.id=" + idComment + "").executeUpdate();            
        /*Message m = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + idMsg + "").getSingleResult();
        if (m != null) {
          //Collection<Message> list = m.getComments();
          //list.add(comment);
          //m.setComments(list);
          //comment.setEstUnComment(m.getId());
          //em.persist(comment);
        } else {
          problemIdMessage = true;
        }*/
      } else {
        problemIdAuthor = true;
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
      throw new DAOExceptionUser(new Status(Status.ERROR_DB), ex.getMessage());
    }

    if (problemIdAuthor) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    } else if (problemIdMessage) {
      throw new DAOExceptionUser(new Status(Status.NOT_ID_MESSAGE));
    }
  }
}
