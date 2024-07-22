package backend;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import gson.GsonSingleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class WelcomeResource {
    private static final Gson gson = GsonSingleton.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcome() {
        JsonObject json = new JsonObject();
        json.addProperty("ok", true);
        json.addProperty("message",
                "Welcome to the API. This endpoint is not intended for human interaction. Check out our repository at https://github.com/CSC207-2024/safeTO");

        return Response.ok(gson.toJson(json)).build();
    }
}
