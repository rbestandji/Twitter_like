package supplies;

import core.CommentaireDAO;
import core.DAOExceptionUser;
import core.GroupeDAO;
import core.MessageDAO;
import core.Status;
import core.UserDAO;
import java.io.IOException;
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
import model.Groupe;
import model.Message;

@Path( "/groupe")
public class Group {

  /*
   * Retourne la liste des messages de l'utilisateur avec l'identifiant en PathParam.
   */
  @Path("/creer/{nom}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirMessagesAvecID(@CookieParam("authCookie") Cookie authenciateCookie,
          @PathParam("nom") String nom) throws IOException {

    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    try {
      GroupeDAO.creerUnGroupe(Long.parseLong(authenciateCookie.getValue()), new Groupe(nom));
      return Response.status(Status.OK).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
