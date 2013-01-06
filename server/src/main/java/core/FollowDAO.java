package core;

import share.core.DAOExceptionUser;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import share.model.User;
import share.model.UserAssignment;
import share.core.Status;

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
      try {
        User userFollower = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idFollower + "")
                .getSingleResult();
        User userFollowing = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idFollowing + "")
                .getSingleResult();

        UserAssignment ass = new UserAssignment(userFollower, userFollowing);
        userFollower.addUserFollowing(ass);
        userFollowing.addUserFollowers(ass);
        em.persist(ass);
      } catch (NoResultException ex) {
        idProblem = true;
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
      try {
        User userFollower = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idFollower + "")
                .getSingleResult();
        User userFollowing = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idFollowing + "")
                .getSingleResult();
        Query q = em.createQuery("SELECT x FROM UserAssignment x WHERE x.follower= :follower and x.following= :following");
        q.setParameter("follower", userFollower);
        q.setParameter("following", userFollowing);
        try {
          UserAssignment ass = (UserAssignment) q.getSingleResult();
          userFollower.removeUserFollowing(ass);
          userFollowing.removeUserFollowers(ass);
          em.remove(ass);
        } catch (NoResultException ex) {
          followProblem = true;
        }
      } catch (NoResultException ex) {
        idProblem = true;
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
    if (follow.equals("following")) {
      followRes = "follower";
    } else {
      followRes = "following";
    }

    List<User> list = new ArrayList<User>();
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
        Query q = em.createQuery("SELECT x." + follow + " FROM UserAssignment x WHERE x." + followRes + "= :followers");
        q.setParameter("followers", user);
        list = (List<User>) q.getResultList();
      } catch (NoResultException ex) {
        idUserError = true;
      }
      utx.commit();
    } catch (Exception ex) {

      Logger.getLogger(FollowDAO.class.getName()).log(Level.SEVERE, null, ex);

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
