package filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            return;
        }
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", Constants.ALLOWED_ORIGIN);
        headers.add("Access-Control-Allow-Methods", Constants.ALLOWED_METHODS);
        headers.add("Access-Control-Allow-Headers", Constants.ALLOWED_HEADERS);
    }
}
