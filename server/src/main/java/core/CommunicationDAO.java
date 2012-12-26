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

public class CommunicationDAO {
  public static void deleteCommunication(Long authorId, Long comId) throws DAOExceptionUser {
    UserTransaction utx = null;
    boolean IdError = false;
    boolean authorIdError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      Communication com = (Communication) em.find(Communication.class, comId);
      User user = (User) em.find(User.class, authorId);
      if (com != null || user !=null) {
        if (com.getAuthor().getId() == authorId) {//vérifie si on est bien le créateur du msg
          if (com.getIsComment()) {
            Comment c = (Comment) com;
            c.getMsgRoot().removeComment(c);
          } else {
            user.removeMessage((Message) com);
          }
          em.remove(com);
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

  /*
   * Fonction cherchant tous les messages et commentaires pour un identifiant donné
   */
  public static List<Communication> getCommunications(Long idUser) throws DAOExceptionUser {
    List<Communication> list = new ArrayList<>();
    UserTransaction utx = null;
    boolean idUserError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      User user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (user != null) {
        Query q = em.createQuery("SELECT x FROM Communication x WHERE x.author.id="+idUser+"");
        //q.setParameter("paraAuthor", user);
        list = (List<Communication>) q.getResultList();
      } else {
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

