package backend;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import user.User;
import user.UserService;

@Path("/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/userinfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        userService.saveUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getUser(@PathParam("id") int id) {
//        User user = userService.getUser(id).orElse(null);
//        if (user == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.ok(user).build();
//    }

    @GET
    @Path("/by-email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) {
        User user = userService.getUserByEmail(email).orElse(null);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
