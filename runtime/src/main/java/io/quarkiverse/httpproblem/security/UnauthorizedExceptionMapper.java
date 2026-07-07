package io.quarkiverse.httpproblem.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.ExceptionMapper;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;

@Priority(Priorities.USER)
@APIResponse(responseCode = "401", description = "Unauthorized: request was not successful because it lacks valid authentication credentials for the requested resource")
public final class UnauthorizedExceptionMapper extends ExceptionMapperBase<UnauthorizedException>
        implements ExceptionMapper<UnauthorizedException> {

    CurrentVertxRequest currentVertxRequest;

    public UnauthorizedExceptionMapper() {
    }

    @Inject
    public UnauthorizedExceptionMapper(PostProcessorsRegistry postProcessorsRegistry,
            CurrentVertxRequest currentVertxRequest) {
        super(postProcessorsRegistry);
        this.currentVertxRequest = currentVertxRequest;
    }

    @Override
    protected HttpProblem toProblem(UnauthorizedException exception) {
        return HttpUnauthorizedUtils.toProblem(currentVertxRequest.getCurrent(), exception)
                .await().indefinitely();
    }

}
