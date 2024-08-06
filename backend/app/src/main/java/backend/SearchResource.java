package backend;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import types.Place;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import builder.RESTfulResponseBuilder;
import geography.Geocoding;
import gson.GsonSingleton;

@Path("/search")
public class SearchResource {
    private static final Gson gson = GsonSingleton.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("address") String query,
            @QueryParam("limit") @DefaultValue("Toronto, ON, Canada") String limit) {
        if (!query.endsWith(limit)) {
            query += " | " + limit;
        }
        Place[] places = Geocoding.resolve(query);
        if (places != null) {
            JsonObject response = new JsonObject();
            response.add("_places", gson.toJsonTree(places));
            JsonObject comments = new JsonObject();
            response.add("_comments", comments);
            return Response.ok(
                    RESTfulResponseBuilder.create()
                            .withOk(true)
                            .withMessage("Upstream API responded successfully")
                            .withData(response)
                            .build())
                    .build();
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                RESTfulResponseBuilder.create()
                        .withOk(false)
                        .withMessage("Upstream API failed to respond")
                        .withData(String.format(
                                "[Trace] Component: Geocoding.resolve(%s)",
                                query))
                        .build())
                .build();
    }
}
