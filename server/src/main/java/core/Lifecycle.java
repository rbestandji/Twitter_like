package core;

import share.core.DAOExceptionUser;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;
import share.model.Message;
import share.model.User;

public class Lifecycle implements ServletContextListener {

  @Resource
  private UserTransaction utx;
  @PersistenceUnit
  private EntityManagerFactory emf;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    if (emf == null) {
      throw new RuntimeException("JPA Persistence Unit is not properly setup!");
    }

    EntityManager em = emf.createEntityManager();
    try {
      utx.begin();
      em.joinTransaction();

      //if (em.createQuery("select c from User c").getResultList().isEmpty()) {
      //création des utilisateurs et insertion en BD
      List<User> users = createUsers();
      for (User u : users) {
        em.persist(u);
      }

      //création de messages et attribution à des utilisateurs de façon aléatoire + insertion en BD
      List<Message> msgs = createMessages();
      for (Message m : msgs) {
        User tmp = users.get((int) (Math.random() * (users.size() - 1)));
        m.setAuthor(tmp);
        tmp.addMessage(m);
        em.persist(m);

      }


      utx.commit();
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

  private List<User> createUsers() throws NoSuchAlgorithmException {
    List<User> users = new ArrayList<User>();
    try {
      users.add(new User("Pasquet", "Jerome", "le.jitou@gmail.com", UserDAO.sha1sum("password")));
      users.add(new User("Laval", "Bernard", "lavalber02@gmail.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Muller", "Lionel", "lionel.muller.34@gmail.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Itam", "Johanna", "le.jojo@gmail.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Nigon", "Julien", "le.julius@gmail.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Froger", "Remi", "trefle4feuille@gmail.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Hermione", "Granger", "hermione@poudlard.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Potter", "Harry", "harry@poudlard.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Gritch", "mechant", "gritch@hyperion.com", UserDAO.sha1sum("motdepasse")));
      users.add(new User("Ender", "str", "ender@strategie.com", UserDAO.sha1sum("motdepasse")));

    } catch (DAOExceptionUser ex) {
      System.out.println("Probleme createUsers " + ex.getMsg());
    }
    return users;
  }

  private List<Message> createMessages() {
    List<String> listText = new ArrayList<String>();
    listText.add("Dis bonjour à papa.");
    listText.add("J'ai de l'amour à revendre !");
    listText.add("SCV READY");
    listText.add("Il en sera ainsi.");
    listText.add("2+3, 12 ? Que voulez vous ?");
    listText.add("Vous pourriez me faire l'historique de la louche à travers les ages ?");
    listText.add("Your drone are under attacks");
    listText.add("Une fleche, un mort.");
    listText.add("Que voulez vous ?");
    listText.add("I'm not the dryad you're looking for.");
    listText.add("Fear the fearsome fury of the forest fawn!");
    listText.add("You communicate by clicking on me; I communicate by doing what you say.");
    listText.add("I'm not in season!");
    listText.add("Fall! Like leaves… in fall!");
    listText.add("I don't reveal much on the mini-map. It's all my fault!");
    listText.add("I'll attract the enemy with my human call: 'I'm so wasted! I'm so wasted!'");





    List<Message> messages = new ArrayList<>();
    for (String l : listText) {
      messages.add(new Message(l, new Date()));
    }
    return messages;
  }
}
