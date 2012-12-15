package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.User;

public class UserDAO {

  //Fonction permettant de créer un compte.
   
  public static void createUser(User user) throws DAOExceptionUser {
    UserTransaction utx = null;
    boolean isValidated = false;
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
        isValidated = true;
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
    if (isValidated) {
      throw new DAOExceptionUser(new Status(Status.EMAIL_VALIDATED));
    }
  }

  //Retourne l'user correspondant à l'id 
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
      throw new DAOExceptionUser(new Status(Status.DB_ERROR), ex.getMessage());
    }
    if (user == null) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }

    return user;
  }

  //Retourne l'id correspondant au mail
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
      throw new DAOExceptionUser(new Status(Status.DB_ERROR), ex.getMessage());
    }

    if (id == null) {
      throw new DAOExceptionUser(new Status(Status.USER_NO_ACCOUNT));
    }


    return id;
  }

  //Retourne la liste des users dont le nom contient la chaine 'name' en paramètre
  public static List<User> searchUser(String name) throws DAOExceptionUser {
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
      query.setParameter("name", "%" + name.toUpperCase() + "%");
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

      throw new DAOExceptionUser(new Status(Status.DB_ERROR), ex.getMessage());
    }

    return users;
  }

  // Connexion 
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
        if (((User) lu.get(0)).getPassword().equals(sha1sum(password))) {
          lu.get(0).setLastLoginDate(new Date());
          user = lu.get(0);
        } else {
          sta = new Status(Status.USER_WRONG_PASSWORD);
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
    if (sta != null) {
      throw new DAOExceptionUser(sta);
    }

    return user;
  }

  private static final String salt = "my twitter-like salt";
  
  public static String sha1sum(String password) throws NoSuchAlgorithmException {
    byte[] hash = null;
    MessageDigest md = MessageDigest.getInstance("SHA1");
    String text = password+salt;
    hash = md.digest(text.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hash.length; ++i) {
      String hex = Integer.toHexString(hash[i]);
      if (hex.length() == 1) {
        sb.append(0);
        sb.append(hex.charAt(hex.length() - 1));
      } else {
        sb.append(hex.substring(hex.length() - 2));
      }
    }
    return sb.toString();
  }

}

