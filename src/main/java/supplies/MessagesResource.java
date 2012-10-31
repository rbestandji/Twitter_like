package supplies;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Message;
import model.User;

@Path( "/messages")
public class MessagesResource {

  /*
   * Permet à l'utilisateur connecté d'envoyer un message 
   * La gestion de mur ou autre n'est pas encore implémenté.
   * Le message est donc juste créé et rattaché à User.
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  @Path( "/envoie")
  public Response envoieMessage(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("msg") String msg) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    Status sta = null;
    UserTransaction utx = null;
    User u_tmp = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaciton begin
      utx.begin();
      em.joinTransaction();
      Message m = new Message(msg, new Date());
      u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + authenciateCookie.getValue() + "").getSingleResult();
      m.setAuteur(u_tmp);

      em.persist(m);
      utx.commit();
      sta = new Status(Status.OK);

    } catch (Exception ex) {
      sta = new Status(Status.ERREUR_BDD);
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
    }

    return Response.ok("Creer un msg " + u_tmp.getNom() + "  " + u_tmp.getId()).status(sta).build();
  }

  /*
   * Retourne la liste des messages de l'utilisateur connecté.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path( "/my")
  public Response obtenirMesMessages(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    return Response.ok(getMessages(Long.parseLong(authenciateCookie.getValue())), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
  }

  /*
   * Retourne la liste des messages de l'utilisateur avec l'identifiant en PathParam.
   */
  @Path("/get/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirMessagesAvecID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    return Response.ok(getMessages(Long.parseLong(id)), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
  }

  /*
   * Retourne la liste des messages de l'utilisateur avec le mail en PathParam.
   */
  @Path("/getuser/{email}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirMessagesAvecEmail(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    Long id;
    try {
      id = UserDAO.getId(email);
      return Response.ok(getMessages(id), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  /*
   * Fonction cherchant tout les messages pour un identifiant donné
   */
  public static List<Message> getMessages(Long id) {
    List<Message> liste = new ArrayList<Message>();
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaciton begin
      utx.begin();
      em.joinTransaction();
      //liste = (List<Message>) em.createQuery("SELECT x FROM Message x WHERE x.auteur=" + id + "").getResultList();
      User user = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + id + "").getSingleResult();

      Query q = em.createQuery("SELECT x FROM Message x WHERE x.auteur= :paraAuteur AND x.estUnCommentaire IS NULL");
      q.setParameter("paraAuteur", user);

      liste = (List<Message>) q.getResultList();

      utx.commit();
      //  em.close();

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
    return liste;
  }

  /*
   * Permet à l'utilisateur connecté de commenter un message
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  @Path( "/envoie/commentaire/{id}")
  public Response envoieCommentaire(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("msg") String msg, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }

    Status sta = null;
    UserTransaction utx = null;
    User u_tmp = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");
      // Transaciton begin
      utx.begin();
      em.joinTransaction();
      Message commentaire = new Message(msg, new Date());
      u_tmp = (User) em.createQuery("SELECT x FROM User x WHERE x.id=" + authenciateCookie.getValue() + "").getSingleResult();
      commentaire.setAuteur(u_tmp);

      Message m = (Message) em.createQuery("SELECT x FROM Message x WHERE x.id=" + id + "").getSingleResult();
      Collection<Message> liste = m.getCommentaires();
      liste.add(commentaire);
      m.setCommentaires(liste);       //Update
      commentaire.setEstUnCommentaire(m.getId());
      em.persist(commentaire);
      utx.commit();
      sta = new Status(Status.OK);

    } catch (Exception ex) {
      sta = new Status(Status.ERREUR_BDD);
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
    }

    return Response.ok("Creer un msg " + u_tmp.getNom() + "  " + u_tmp.getId()).status(sta).build();
  }
}
