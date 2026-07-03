package io.quarkiverse.httpproblem.client;

import io.quarkiverse.httpproblem.HttpProblem;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    @GET
    @Path("/throw")
    @Produces()
    public Response throwProblem() {
        throw HttpProblem.builder()
                .withStatus(409)
                .withTitle("Conflict from upstream service")
                .withDetail("Nothing to add")
                .build();
    }

    @Inject
    @RestClient
    SelfRestClient selfClient;

    @GET
    @Path("/throw-via-rest-client")
    public void throwViaRestClient() {
        selfClient.doThrow();
    }

    @Inject
    @RestClient
    SelfRestClientWithExceptionMapper selfClientWithMapper;

    @GET
    @Path("/throw-via-rest-client-with-mapper")
    public void throwViaRestClientWithMapper() {
        selfClientWithMapper.doThrow();
    }

    /**
     * Returns a malformed application/problem+json body (not valid JSON).
     **/
    @GET
    @Path("/throw-malformed")
    @Produces("application/problem+json")
    public Response throwMalformedProblem() {
        return Response.status(409)
                .type("application/problem+json")
                .entity("this is not valid json {{{")
                .build();
    }

    /**
     * Returns application/problem+json with charset parameter appended.
     */
    @GET
    @Path("/throw-with-charset")
    @Produces("application/problem+json;charset=UTF-8")
    public Response throwProblemWithCharset() {
        throw HttpProblem.builder()
                .withStatus(409)
                .withTitle("Conflict from upstream service")
                .withDetail("Nothing to add")
                .build();
    }

    @Inject
    @RestClient
    SelfRestClientWithExceptionMapper selfClientWithMapperMalformed;

    @GET
    @Path("/throw-via-rest-client-with-mapper-malformed")
    public void throwViaRestClientMalformed() {
        selfClientWithMapperMalformed.doThrowMalformed();

    }

    @GET
    @Path("/throw-via-rest-client-with-mapper-charset")
    public void throwViaRestClientWithCharset() {
        selfClientWithMapperMalformed.doThrowWithCharset();
    }

}