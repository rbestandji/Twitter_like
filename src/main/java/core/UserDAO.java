package core;

import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.Group;
import model.User;

public class UserDAO {

  /*
   * Fonction permettant de créer un compte.
   */
  public static void createUser(User user) throws DAOExceptionUser {
    UserTransaction utx = null;
    boolean isTaken = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      List<User> lu = em.createQuery("SELECT x FROM User x WHERE x.email='" + user.getEmail() + "'").getResultList();
      if (lu.isEmpty()) {
        em.persist(user);
      } else {
        isTaken = true;
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
    if (isTaken) {
      throw new DAOExceptionUser(new Status(Status.EMAIL_TAKEN));
    }
  }

  /*Retourne l'user correspondant à l'id */
  public static User getUser(Long id) throws DAOExceptionUser {
    User user = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaction begin
      utx.begin();
      em.joinTransaction();

      user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
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
    if (user == null) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }

    return user;
  }

  /*Retourne l'id correspondant au mail*/
  public static Long getId(String email) throws DAOExceptionUser {
    Long id = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      // Transaction begin
      utx.begin();
      em.joinTransaction();
      List<User> lu = em.createQuery("SELECT x FROM User x WHERE x.email='" + email + "'").getResultList();
      if (!lu.isEmpty()) {
        id = lu.get(0).getId();
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
      }
      throw new DAOExceptionUser(new Status(Status.ERROR_DB), ex.getMessage());
    }

    if (id == null) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }


    return id;
  }

  /*Retourne la liste des users dont le nom contient la chaine 'nom' en paramètre*/
  public static List<User> searchUser(String nom) throws DAOExceptionUser {
    List<User> users = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaction begin
      utx.begin();
      em.joinTransaction();
      Query query = em.createQuery("SELECT x FROM User x WHERE UPPER(x.name) LIKE :name");
      query.setParameter("name", "%" + nom.toUpperCase() + "%");
      users = query.getResultList();
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

    return users;
  }

  /* Connexion */
  public static User connection(String email, String password) throws DAOExceptionUser {
    User user = null;
    Status sta = null;

    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      utx.begin();
      em.joinTransaction();
      List<User> lu = em.createQuery("SELECT x FROM User x WHERE x.email='" + email + "'").getResultList();
      if (lu.isEmpty()) {
        sta = new Status(Status.USER_NO_ACCOUNT);
      } else {
        if (((User) lu.get(0)).getPassword().equals(password)) {
          user = lu.get(0);
        } else {
          sta = new Status(Status.USER_BAD_PASSWORD);
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
      throw new DAOExceptionUser(new Status(Status.ERROR_DB), ex.getMessage());
    }
    if (sta != null) {
      throw new DAOExceptionUser(sta);
    }

    return user;
  }
}
