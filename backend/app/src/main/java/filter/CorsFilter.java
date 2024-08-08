package filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", Constants.ALLOWED_ORIGIN);
        responseContext.getHeaders().add("Access-Control-Allow-Methods", Constants.ALLOWED_METHODS);
        // responseContext.getHeaders().add("Access-Control-Allow-Headers",
        // "Content-Type, Authorization");
    }
}
