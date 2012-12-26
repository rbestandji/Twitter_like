package core;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import share.core.DAOExceptionUser;
import share.core.Status;
import share.model.Comment;
import share.model.Communication;
import share.model.Message;
import share.model.User;

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
        uTmp.addMessage(message);
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

  public static void deleteMessage(Long authorId, Long msgId) throws DAOExceptionUser {

    UserTransaction utx = null;
    boolean IdError = false;
    boolean authorIdError = false;
    List<Comment> list = new ArrayList<>();
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();

      Message msg = (Message) em.find(Message.class, msgId);
      User user = (User) em.find(User.class, authorId);
      if (msg != null || user !=null) {
        if (msg.getAuthor().getId() == authorId) {//vérifie si on est bien le créateur du msg
          user.removeMessage(msg);
          em.remove(msg);
        } else {
          authorIdError = true;
        }
      } else {
        IdError = true;
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

    if (IdError) {
      throw new DAOExceptionUser(new Status(Status.ID_NOT_EXIST));
    }
    if (authorIdError) {
      throw new DAOExceptionUser(new Status(Status.WRONG_USER));
    }
  }
}