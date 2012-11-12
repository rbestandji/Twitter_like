package supplies;

import core.CommentDAO;
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
  @Path( "/send")
  public Response sendMessage(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("msg") String msg) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
    }
    Message message = new Message(msg, new Date());
    try {
      MessageDAO.sendMessage(Long.parseLong(authenciateCookie.getValue()), message);
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
  public Response getMesMessages(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
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
  public Response getMessageWithID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
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
  public Response getMessagesWithEmail(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
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
  @Path( "/send/comment/{id}")
  public Response sendComment(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("msg") String msg, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
    }
    Message comment = new Message(msg, new Date());
    try {
      CommentDAO.sendComment(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(id), comment);
     return Response.ok(comment).status(Status.OK).build();
    } catch (DAOExceptionUser ex) {
    return Response.status(ex.getStatus()).build();
    }
  }
  
   /*
   * Permet à l'utilisateur connecté de supprimer l'un de ses messages
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  @Path( "/delete/{idMsg}")
  public Response deleteIdMessage(@CookieParam("authCookie") Cookie authenciateCookie,
          @PathParam("idMsg") String idMsg) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_NOT_LOGGED_IN)).build();
    }
    
    try {
      MessageDAO.deleteMessage(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(idMsg));
      return Response.status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
