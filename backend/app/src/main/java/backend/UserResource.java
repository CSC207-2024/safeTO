package backend;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import user.UserService;
import user.User;

@Path("/user")
public class UserResource {

    private final UserService userService = new UserService();

    @POST
    @Path("/userinfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        userService.saveUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
