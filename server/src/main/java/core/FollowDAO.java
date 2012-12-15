package core;

import java.util.ArrayList;
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
    boolean idProblem = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      utx.begin();
      em.joinTransaction();
      User userFollower = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollower + "'").getSingleResult();
      User userFollowing = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollowing + "'").getSingleResult();
      if (userFollower == null || userFollowing == null) {
        idProblem = true;
      } else {
        UserAssignment ass = new UserAssignment(userFollower, userFollowing);
        userFollower.getUsersFollowing().add(ass);
        userFollowing.getUsersFollowers().add(ass);
        em.persist(ass);
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
    if (idProblem) {
      throw new DAOExceptionUser(new Status(Status.ID_NOT_EXIST));
    }

  }

  public static void stopFollowUser(long idFollower, long idFollowing) throws DAOExceptionUser {
    UserTransaction utx = null;
    boolean idProblem = false;
    boolean followProblem = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      utx.begin();
      em.joinTransaction();
      User userFollower = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollower + "'").getSingleResult();
      User userFollowing = (User) em.createQuery("SELECT x FROM User x WHERE x.id='" + idFollowing + "'").getSingleResult();
      if (userFollower == null || userFollowing == null) {
        idProblem = true;
      } else {
        Query q = em.createQuery("SELECT x FROM UserAssignment x WHERE x.follower= :follower and x.following= :following");
        q.setParameter("follower", userFollower);
        q.setParameter("following", userFollowing);
        UserAssignment ass = (UserAssignment) q.getSingleResult();
        if (ass == null) {
          followProblem = true;
        } else {
          userFollower.getUsersFollowing().remove(ass);
          userFollowing.getUsersFollowers().remove(ass);
          em.remove(ass);
        }
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
    if (idProblem) {
      throw new DAOExceptionUser(new Status(Status.ID_NOT_EXIST));
    }
    if (followProblem) {
      throw new DAOExceptionUser(new Status(Status.NOT_FOLLOWING));
    }
  }

  /*
   * Fonction cherchant tous les abonnements ou abonnés pour un identifiant donné
   */
  public static List<User> getFollows(Long idUser, String follow) throws DAOExceptionUser {
    String followRes;
    if (follow.equals("following"))
      followRes="follower";
    else
      followRes="following";

    List<User> list = new ArrayList<User>();
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
        Query q = em.createQuery("SELECT x."+ follow +" FROM UserAssignment x WHERE x." + followRes +"= :followres");
        q.setParameter("followres", user);
        list = (List<User>) q.getResultList();
      }else {
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
