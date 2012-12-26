package supplies;

import core.CommentDAO;
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
import share.core.DAOExceptionUser;
import share.core.Status;
import share.model.Comment;

@Path( "/communications/comments")
public class CommentsResource {
  /*
   * Permet à l'utilisateur connecté de commenter un message
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  @Path( "/send/{idMsg}")
  public Response sendComment(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("msg") String msg, @PathParam("idMsg") String idMsg) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    Comment comment = new Comment(msg, new Date());
    try {
      CommentDAO.sendComment(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(idMsg), comment);
     return Response.ok(comment).status(Status.OK).build();
    } catch (DAOExceptionUser ex) {
    return Response.status(ex.getStatus()).build();
    }
  }
  
  
}
