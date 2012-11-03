package supplies;

import core.CommentaireDAO;
import core.DAOExceptionUser;
import core.MessageDAO;
import core.Status;
import core.UserDAO;
import java.util.Date;
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

@Path( "/messages")
public class MessagesResource {

  /*
   * Permet à l'utilisateur connecté d'envoyer un message 
   * La gestion de mur ou autre n'est pas encore implémentée.
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
    Message message = new Message(msg, new Date());
    try {
      MessageDAO.envoieMessage(Long.parseLong(authenciateCookie.getValue()), message);
      return Response.status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
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
    try {
      return Response.ok(MessageDAO.getMessages(Long.parseLong(authenciateCookie.getValue())), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
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
    try {
      return Response.ok(MessageDAO.getMessages(Long.parseLong(id)), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
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
      return Response.ok(MessageDAO.getMessages(id), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
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
    Message commentaire = new Message(msg, new Date());
    try {
      CommentaireDAO.envoieCommentaire(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(id), commentaire);
     return Response.ok(commentaire).status(Status.OK).build();
    } catch (DAOExceptionUser ex) {
    return Response.status(ex.getStatus()).build();
    }
  }
}
