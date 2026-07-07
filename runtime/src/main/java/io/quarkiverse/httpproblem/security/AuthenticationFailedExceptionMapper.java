package io.quarkiverse.httpproblem.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;

import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;

@Priority(Priorities.USER - 1)
public final class AuthenticationFailedExceptionMapper extends ExceptionMapperBase<AuthenticationFailedException> {

    CurrentVertxRequest currentVertxRequest;

    public AuthenticationFailedExceptionMapper() {
    }

    @Inject
    public AuthenticationFailedExceptionMapper(PostProcessorsRegistry postProcessorsRegistry,
            CurrentVertxRequest currentVertxRequest) {
        super(postProcessorsRegistry);
        this.currentVertxRequest = currentVertxRequest;
    }

    @Override
    protected HttpProblem toProblem(AuthenticationFailedException exception) {
        return HttpUnauthorizedUtils.toProblem(currentVertxRequest.getCurrent(), exception)
                .await().indefinitely();
    }

}
