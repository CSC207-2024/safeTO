package backend;

import analysis.facade.CrimeAnalysisFacade;
import builder.RESTfulResponseBuilder;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Path("/analysis")
public class AnalysisResource {
    private static final String ANALYSIS_JAR_PATH = "path/to/analysis.jar"; // Update this path as necessary

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
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Combine stderr and stdout
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor();
        } catch (Exception e) {
            return RESTfulResponseBuilder.create()
                    .withOk(false)
                    .withMessage("Error executing command: " + e.getMessage())
                    .withData(null)
                    .build();
        }
        return RESTfulResponseBuilder.create()
                .withMessage("Command executed successfully")
                .withData(output.toString())
                .build();
    }
}
