package io.quarkiverse.httpproblem.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkiverse.httpproblem.postprocessing.ProblemContext;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

public final class UnauthorizedExceptionReactiveMapper {

    private final PostProcessorsRegistry postProcessorsRegistry;

    @Inject
    public UnauthorizedExceptionReactiveMapper(PostProcessorsRegistry postProcessorsRegistry) {
        this.postProcessorsRegistry = postProcessorsRegistry;
    }

    @ServerExceptionMapper(value = UnauthorizedException.class, priority = Priorities.USER - 1)
    @APIResponse(responseCode = "401", description = "Unauthorized: request was not successful because it lacks valid authentication credentials for the requested resource")
    public Uni<Response> handle(RoutingContext routingContext, UnauthorizedException exception) {
        return HttpUnauthorizedUtils.toProblem(routingContext, exception)
                .map(problem -> {
                    ProblemContext context = ProblemContext.of(exception, routingContext.normalizedPath());
                    HttpProblem finalProblem = postProcessorsRegistry.applyPostProcessing(problem,
                            context);
                    return finalProblem.toResponse();
                });
    }

}
