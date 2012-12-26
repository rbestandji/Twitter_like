package supplies;

import core.CommunicationDAO;
import share.core.DAOExceptionUser;
import core.MessageDAO;
import share.core.Status;
import java.util.Date;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import share.model.Message;

@Path( "/communications/messages")
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
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    Message message = new Message(msg, new Date());
    try {
      MessageDAO.sendMessage(Long.parseLong(authenciateCookie.getValue()), message);
      return Response.status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
  
}
