package backend;

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
import geography.ReverseGeocoding;
import gson.GsonSingleton;

@Path("/lookup")
public class LookupResource {
    private static final Gson gson = GsonSingleton.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookup(@QueryParam("lat") float latitude, @QueryParam("long") float longitude) {
        Place place = ReverseGeocoding.resolve(latitude, longitude);
        if (place != null) {
            JsonObject response = new JsonObject();
            response.add("_place", gson.toJsonTree(place));
            response.addProperty("postalCode", place.getAddress().getPostcode());
            response.addProperty("neighbourhood", place.getAddress().getNeighbourhood());
            JsonObject comments = new JsonObject();
            comments.addProperty("is_neighbourhood_in_toronto_158", null);
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
                                "[Trace] Component: ReverseGeocoding.resolve(%f, %f)",
                                latitude,
                                longitude))
                        .build())
                .build();
    }
}
