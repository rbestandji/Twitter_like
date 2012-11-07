package core;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import model.User;
import model.Message;

public class MessageDAO {

  public static void envoieMessage(Long id, Message message) throws DAOExceptionUser {

    UserTransaction utx = null;
    boolean erreurId = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      User u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
      if (u_tmp != null) {
        message.setAuteur(u_tmp);
        em.persist(message);
      } else {
        erreurId = true;
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

    if (erreurId) {
      throw new DAOExceptionUser(new Status(Status.UTILISATEUR_PAS_DE_COMPTE));
    }
  }

  /*
   * Fonction cherchant tout les messages pour un identifiant donné
   */
  public static List<Message> getMessages(Long id) throws DAOExceptionUser {
    List<Message> liste = new ArrayList<Message>();
    UserTransaction utx = null;
    boolean erreurId = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      utx.begin();
      em.joinTransaction();
      User user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();
      if (user != null) {
        Query q = em.createQuery("SELECT x FROM Message x WHERE x.auteur= :paraAuteur AND x.estUnCommentaire IS NULL");
        q.setParameter("paraAuteur", user);
        liste = (List<Message>) q.getResultList();
      } else {
        erreurId = true;
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
    if (erreurId) {
      throw new DAOExceptionUser(new Status(Status.UTILISATEUR_PAS_DE_COMPTE));
    }
    return liste;
  }
}
