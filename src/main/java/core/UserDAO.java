package core;

import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.User;

public class UserDAO {

  /*
   * Fonction permettant de creer un compte.
   */
  public static void createUser(User user) throws DAOExceptionUser {
    UserTransaction utx = null;
    boolean estPrise = false;
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
        estPrise = true;
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
      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }
    if (estPrise) {
      throw new DAOExceptionUser(new Status(Status.EMAIL_PRISE));
    }
  }

  /*
   * Retourne l'utilisateur correspondant à l'id.
   */
  public static User getUser(Long id) throws DAOExceptionUser {
    User user = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaciton begin
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
      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }
    if (user == null) {
      throw new DAOExceptionUser(new Status(Status.UTILISATEUR_PAS_DE_COMPTE));
    }

    return user;
  }

  /*
   * Retourne l'id correspondant au mail.
   */
  public static Long getId(String email) throws DAOExceptionUser {
    Long id = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      // Transaciton begin
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
      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }

    if (id == null) {
      throw new DAOExceptionUser(new Status(Status.UTILISATEUR_PAS_DE_COMPTE));
    }


    return id;
  }

  /*
   * Retourne la liste des utilisateurs dont le nom contient la chaine 'nom' en paramètre.
   */
  public static List<User> searchUser(String nom) throws DAOExceptionUser {
    List<User> users = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaciton begin
      utx.begin();
      em.joinTransaction();
      Query query = em.createQuery("SELECT x FROM User x WHERE UPPER(x.nom) LIKE :nom");
      query.setParameter("nom", "%" + nom.toUpperCase() + "%");
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

      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }

    return users;
  }

  /* Connection */
  public static User connection(String email, String mdp) throws DAOExceptionUser {
    User utilisateur = null;
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
        sta = new Status(Status.UTILISATEUR_PAS_DE_COMPTE);
      } else {
        if (((User) lu.get(0)).getMdp().equals(mdp)) {
          utilisateur = lu.get(0);
        } else {
          sta = new Status(Status.UTILISATEUR_MAUVAIS_MOT_PASS);
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
      throw new DAOExceptionUser(new Status(Status.ERREUR_BDD), ex.getMessage());
    }
    if (sta != null) {
      throw new DAOExceptionUser(sta);
    }

    return utilisateur;
  }
}
