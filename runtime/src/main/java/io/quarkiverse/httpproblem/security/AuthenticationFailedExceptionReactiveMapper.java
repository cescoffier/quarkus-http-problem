package io.quarkiverse.httpproblem.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkiverse.httpproblem.postprocessing.ProblemContext;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

public final class AuthenticationFailedExceptionReactiveMapper {

    private final PostProcessorsRegistry postProcessorsRegistry;

    @Inject
    public AuthenticationFailedExceptionReactiveMapper(PostProcessorsRegistry postProcessorsRegistry) {
        this.postProcessorsRegistry = postProcessorsRegistry;
    }

    @ServerExceptionMapper(value = AuthenticationFailedException.class, priority = Priorities.USER - 1)
    public Uni<Response> handle(RoutingContext routingContext, AuthenticationFailedException exception) {
        return HttpUnauthorizedUtils.toProblem(routingContext, exception)
                .map(problem -> {
                    ProblemContext context = ProblemContext.of(exception, routingContext.normalizedPath());
                    HttpProblem finalProblem = postProcessorsRegistry.applyPostProcessing(problem,
                            context);
                    return finalProblem.toResponse();
                });
    }

}
