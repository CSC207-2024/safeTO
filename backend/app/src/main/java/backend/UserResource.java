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

    @GET
    @Path("/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userID") String userID) {
        User user = userService.getUserByUserID(userID).orElse(null);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }


    @GET
    @Path("/{userID}/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailByUserID(@PathParam("userID") String userID) {
        Optional<String> emailOpt = userService.getEmailByUserID(userID);
        if (emailOpt.isPresent()) {
            return Response.ok(emailOpt.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
