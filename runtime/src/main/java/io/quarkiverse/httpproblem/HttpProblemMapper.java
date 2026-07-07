package io.quarkiverse.httpproblem;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;

import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;

@Priority(Priorities.USER)
public final class HttpProblemMapper extends ExceptionMapperBase<HttpProblem> {

    public HttpProblemMapper() {
    }

    @Inject
    public HttpProblemMapper(PostProcessorsRegistry postProcessorsRegistry) {
        super(postProcessorsRegistry);
    }

    @Override
    protected HttpProblem toProblem(HttpProblem exception) {
        return exception;
    }
}
