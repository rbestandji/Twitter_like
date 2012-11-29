package core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;
import model.User;

public class Lifecycle implements ServletContextListener {

  @Resource
  private UserTransaction utx;
  @PersistenceUnit
  private EntityManagerFactory emf;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
      System.out.println("DEMARRAGE");

    if (emf == null) {
      throw new RuntimeException("JPA Persistence Unit is not properly setup!");
    }

    EntityManager em = emf.createEntityManager();
      System.out.println("DEMARRAGE");

    try {
      utx.begin();
      em.joinTransaction();
      if (em.createQuery("select c from User c").getResultList().isEmpty()) {
        List<User> users = createUsers();
        for (User u : users) {
          em.persist(u);
        }
        utx.commit();
      }
    } catch (Exception ex) {
      try {
        utx.setRollbackOnly();
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
      throw new RuntimeException(ex.getMessage(), ex);
    } finally {
      if (em != null) {
        em.close();
      }
    }

  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }

  private List<User> createUsers() {
    List<User> users = new ArrayList<User>();
    try {
      users.add(new User("Pasquet", "Jerome", "le.jitou@gmail.com", "password"));
      users.add(new User("Laval", "Benard", "lavalber02@gmail.com", "motdepasse"));
      users.add(new User("Muller", "Lionel", "lionel.muller.34@gmail.com", "motdepasse"));
      users.add(new User("Itam", "Johanna", "le.jojo@gmail.com", "motdepasse"));
      users.add(new User("Nigon", "Julien", "le.julius@gmail.com", "motdepasse"));
      users.add(new User("Froger", "Remi", "trefle4feuille@gmail.com", "motdepasse"));
      users.add(new User("Hermione", "Granger", "hermione@poudlard.com", "motdepasse"));
      users.add(new User("Potter", "Harry", "harry@poudlard.com", "motdepasse"));
      users.add(new User("Gritch", "mechant", "gritch@hyperion.com", "motdepasse"));
      users.add(new User("Ender", "str", "ender@strategie.com", "motdepasse"));

    } catch (DAOExceptionUser ex) {
      System.out.println("Probleme createUsers " + ex.getMsg());
    }


    return users;
  }
}
