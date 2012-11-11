package core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.transaction.UserTransaction;
import model.Group;
import model.User;

public class GroupDAO {
  /*
   * Permet à l'utilisateur connecté de créer un groupe
   */

  public static void createGroup(Long idUser, Group group) throws DAOExceptionUser, IOException {
    UserTransaction utx = null;
    boolean idError = false;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      em.setFlushMode(FlushModeType.COMMIT);
      utx.begin();
      em.joinTransaction();
      User uTmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + idUser + "").getSingleResult();
      if (uTmp != null) {
        Group g = new Group(group.getName(), uTmp);
        //sg.setCreateur(uTmp);

        uTmp.getGroups().add(g);
        //em.persist(group);


      } else {
        idError = true;
      }
      utx.commit();

    } catch (Exception ex) {
      Logger logger = Logger.getLogger("logger");
      FileHandler fh = new FileHandler("monLog.txt");
      logger.addHandler(fh);
      logger.log(Level.INFO, "Erreur BDD dans GroupDAO : " + idUser
              + "\n" + ex.getMessage()
              + "\n" + ex.getLocalizedMessage());
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
