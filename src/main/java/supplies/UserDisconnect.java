package supplies;

import core.Status;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import model.Message;

@Path("/bye")
public class UserDisconnect {

  @GET
  @Produces( MediaType.APPLICATION_JSON)
public Response deconnection() 
{ 
   NewCookie cookie = new NewCookie("authCookie", "-1", "/" ,"localhost", "", 0, false); 
   return Response.ok("Deconnection !!!!", MediaType.APPLICATION_JSON).cookie(cookie).build();
} 

}
