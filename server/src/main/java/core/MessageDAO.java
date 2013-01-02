package core;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import share.core.DAOExceptionUser;
import share.core.Status;
import share.model.Message;
import share.model.User;

public class MessageDAO {

  public static void deleteMessage(Long authorId, Long msgId) throws DAOExceptionUser {

    UserTransaction utx = null;
    boolean msgIdError = false;
    boolean authorIdError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      try {
        Message msg = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + msgId + "").getSingleResult();      
        if (msg.getAuthor().getId() == authorId) {
          Message root = msg.getMsgRoot();
          if (root != null) { // Si c'est un commentaire.
            root.getComments().remove(msg);
          }
          em.remove(msg);
        } else {
          authorIdError = true;         
        }
      } catch (NoResultException ex) {
        msgIdError = true;
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

    if (msgIdError) {
      throw new DAOExceptionUser(new Status(Status.ID_NOT_EXIST));
    }
    if (authorIdError) {
      throw new DAOExceptionUser(new Status(Status.WRONG_USER));
    }
  }

  public static void sendMessage(Long id, Message message) throws DAOExceptionUser {

    UserTransaction utx = null;
    boolean idError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      try {
        User uTmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
        message.setAuthor(uTmp);
        uTmp.addMessage(message);
        em.persist(message);
      } catch(NoResultException ex) {
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

  public static List<Message> getMessages(Long idUser) throws DAOExceptionUser {
    List<Message> list = new ArrayList<Message>();
    UserTransaction utx = null;
    boolean idUserError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      try {
        User user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
        Query q = em.createQuery("SELECT x FROM Message x WHERE x.author= :paraAuthor AND x.msgRoot IS NULL");
        q.setParameter("paraAuthor", user);
        list = (List<Message>) q.getResultList();
      } catch (NoResultException ex) {
        idUserError = true;
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
    if (idUserError) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }
    return list;
  }
}