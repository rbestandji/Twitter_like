package core;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.User;
import model.Message;

public class MessageDAO {

  public static void sendMessage(Long id, Message message) throws DAOExceptionUser {

    UserTransaction utx = null;
    boolean idError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      User uTmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
      if (uTmp != null) {
        message.setAuthor(uTmp);
        em.persist(message);
      } else {
        idError = true;
      }
      utx.commit();

    } catch (Exception ex) {
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
      }
      throw new DAOExceptionUser(new Status(Status.DB_ERROR), ex.getMessage());
    }

    if (idError) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }
  }

  /*
   * Fonction cherchant tous les messages pour un identifiant donné
   */
  public static List<Message> getMessages(Long id) throws DAOExceptionUser {
    List<Message> list = new ArrayList<Message>();
    UserTransaction utx = null;
    boolean idError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      User user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
      if (user != null) {
        Query q = em.createQuery("SELECT x FROM Message x WHERE x.author= :paraAuthor AND x.isComment IS NULL");
        q.setParameter("paraAuthor", user);
        list = (List<Message>) q.getResultList();
      } else {
        idError = true;
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
    if (idError) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }
    return list;
  }
}
