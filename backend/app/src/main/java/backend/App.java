/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package backend;

import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.ApplicationPath;
import filter.CorsFilter;

@ApplicationPath("/")
public class App extends ResourceConfig {
    public App() {
        packages("backend");
        register(CorsFilter.class);
    }
}