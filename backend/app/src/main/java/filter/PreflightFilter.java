package filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@PreMatching
public class PreflightFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            requestContext.abortWith(Response.noContent()
                    .header("Access-Control-Allow-Origin", Constants.ALLOWED_ORIGIN)
                    .header("Access-Control-Allow-Methods", Constants.ALLOWED_METHODS)
                    .header("Access-Control-Max-Age", "86400")
                    .build());
        }
    }
}
