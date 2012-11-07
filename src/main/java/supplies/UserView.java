package supplies;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import java.util.List;
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

    User user;
    try {
      user = UserDAO.getUser(Long.parseLong(id));
      user.enleverProblemeRecursivite();
      return Response.ok(user, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  @Path("/getuser/{email}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirUserAvecMail(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    Long id;
    try {
      id = UserDAO.getId(email);
      User user = UserDAO.getUser(id);
      user.enleverProblemeRecursivite();
      return Response.ok(user, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }

  }

  @Path("/search/{nom}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenirUsersAvecNom(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("nom") String nom) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.UTILISATEUR_PAS_CONNECTE)).build();
    }
    try {
      List<User> u = UserDAO.searchUser(nom);
      for (int i = 0; i < u.size(); i++) {
        u.get(i).enleverProblemeRecursivite();
      }
      return Response.ok(u, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
