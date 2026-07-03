package io.quarkiverse.httpproblem.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "self")
public interface SelfRestClient {
    @GET
    @Path("/throw")
    void doThrow();

    @GET
    @Path("/throw-malformed")
    // The @Produces is only necessary for RESTEasy Classic. Quarkus REST is more permissive.
    @Produces({MediaType.APPLICATION_JSON, "application/problem+json"})
    void doThrowMalformed();

    @GET
    @Path("/throw-with-charset")
    // The @Produces is only necessary for RESTEasy Classic. Quarkus REST is more permissive.
    @Produces({MediaType.APPLICATION_JSON, "application/problem+json"})
    void doThrowWithCharset();
}