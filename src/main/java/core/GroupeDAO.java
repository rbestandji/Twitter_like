package core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.transaction.UserTransaction;
import model.Groupe;
import model.User;

public class GroupeDAO {
  /*
   * Permet à l'utilisateur connecté de creer un groupe
   */

  public static void creerUnGroupe(Long idUser, Groupe groupe) throws DAOExceptionUser, IOException {
    UserTransaction utx = null;
    boolean erreurId = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      em.setFlushMode(FlushModeType.COMMIT);
      utx.begin();
      em.joinTransaction();
      User u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (u_tmp != null) {
        Groupe g = new Groupe(groupe.getNom(), u_tmp);
        //sg.setCreateur(u_tmp);

        u_tmp.getListeDesGroupe().add(g);
        //em.persist(groupe);


      } else {
        erreurId = true;
      }
      utx.commit();

    } catch (Exception ex) {
      Logger logger = Logger.getLogger("logger");
      FileHandler fh = new FileHandler("monLog.txt");
      logger.addHandler(fh);
      logger.log(Level.INFO, "Erreur BDD dans GroupeDAO : " + idUser
              + "\n" + ex.getMessage()
              + "\n" + ex.getLocalizedMessage());
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
}
