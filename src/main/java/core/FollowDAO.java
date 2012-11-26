package core;

import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.User;
import model.UserAssignment;

public class FollowDAO {

  //Fonction permettant à un utilisateur (User) de suivre un autre utilisateur (User)
  public static void followUser(Long idFollower, Long idFollowing) throws DAOExceptionUser {
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      
      utx.begin();
      em.joinTransaction();
      User userFollower = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollower + "'").getSingleResult();
      User userFollowing = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollowing + "'").getSingleResult();
      UserAssignment ass = new UserAssignment(userFollower, userFollowing);
      userFollower.getUsersFollowing().add(ass);
      userFollowing.getUsersFollowers().add(ass);
      em.persist(ass);
      
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

  }
}
