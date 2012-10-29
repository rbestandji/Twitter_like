package supplies;

import core.Status;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.User;

@Path( "/users")
public class UserView {

  /*
   * Retourne la description de l'utilisateur
   */
  @Path("/get/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirUserAvecID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    return Response.ok(getUser(Long.parseLong(id)), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
  }

  @Path("/getuser/{email}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirUserAvecMail(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    Long id = getId(email);
    if (id == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_DE_COMPTE)).build();
    } else {
      return Response.ok(getUser(id), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    }
  }

  @Path("/search/{nom}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirUsersAvecNom(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("nom") String nom) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }

    return Response.ok(searchUser(nom), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
  }

  public static List<User> searchUser(String nom) {
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
    }
    return users;
  }

  public static User getUser(Long id) {
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
    }
    return user;
  }

  public static Long getId(String email) {
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
    }
    return id;
  }
}
