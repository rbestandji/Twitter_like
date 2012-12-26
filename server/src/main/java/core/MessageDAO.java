package core;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import share.core.DAOExceptionUser;
import share.core.Status;
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
}