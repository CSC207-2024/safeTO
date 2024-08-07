package backend;

import builder.RESTfulResponseBuilder;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gson.GsonSingleton;

@Path("/analysis")
public class AnalysisResource {
    private static final String ANALYSIS_JAR_PATH;
    private static final Gson gson = GsonSingleton.getInstance();

    static {
        String analysisJarPath = System.getenv("SAFETO_ANALYSIS_PATH");
        if (analysisJarPath == null) {
            analysisJarPath = "/home/vixen-kite-celery/safeto-analysis/analysis.jar";
        }
        ANALYSIS_JAR_PATH = analysisJarPath;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/break-and-enter")
    public Response breakAndEnter(
            @QueryParam("latitude") double latitude,
            @QueryParam("longitude") double longitude,
            @QueryParam("radius") int radius,
            @QueryParam("threshold") int threshold) {

        String[] command = {
                "java",
                "-jar",
                ANALYSIS_JAR_PATH,
                "break_enter",
                String.valueOf(latitude),
                String.valueOf(longitude),
                String.valueOf(radius),
                String.valueOf(threshold)
        };

        String result = executeCommand(command);
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/auto-theft")
    public Response autoTheft(
            @QueryParam("latitude") double latitude,
            @QueryParam("longitude") double longitude,
            @QueryParam("radius") int radius,
            @QueryParam("threshold") int threshold,
            @QueryParam("earliestYear") int earliestYear) {

        String[] command = {
                "java",
                "-jar",
                ANALYSIS_JAR_PATH,
                "auto_theft",
                String.valueOf(latitude),
                String.valueOf(longitude),
                String.valueOf(radius),
                String.valueOf(threshold),
                String.valueOf(earliestYear)
        };

        String result = executeCommand(command);
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ranking")
    public Response ranking(
            @QueryParam("neighborhood") String neighborhood,
            @QueryParam("specificCrime") String specificCrime) {

        String[] command = {
                "java",
                "-jar",
                ANALYSIS_JAR_PATH,
                "ranking",
                neighborhood,
                specificCrime != null ? specificCrime : ""
        };

        String result = executeCommand(command);
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response list(@QueryParam("collection") String collection) {
        // Logic to invoke the command to list entries in the collection
        // Assuming the command to list looks like this
        String[] command = {
                "java",
                "-jar",
                ANALYSIS_JAR_PATH,
                "list",
                collection
        };

        String result = executeCommand(command);
        return Response.ok(result).build();
    }

    private String executeCommand(String[] command) {
        StringBuilder output = new StringBuilder();
        JsonObject responseData = new JsonObject();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor();
            String outputString = output.toString();
            responseData.addProperty("_raw", outputString.substring(0, Math.min(outputString.length(), 160)));
            JsonElement parsedResult = gson.fromJson(outputString, JsonElement.class);

            responseData.add("result", parsedResult);
        } catch (Exception e) {
            return RESTfulResponseBuilder.create()
                    .withOk(false)
                    .withMessage("Error executing command: " + e.getMessage())
                    .withData(responseData)
                    .build();
        }
        return RESTfulResponseBuilder.create()
                .withMessage("Command executed successfully")
                .withData(responseData)
                .build();
    }
}
