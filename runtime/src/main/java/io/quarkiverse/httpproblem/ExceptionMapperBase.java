package io.quarkiverse.httpproblem;

import java.util.Objects;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;

import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkiverse.httpproblem.postprocessing.ProblemContext;

public abstract class ExceptionMapperBase<E extends Throwable> implements ExceptionMapper<E> {

    private PostProcessorsRegistry postProcessorsRegistry;

    @Context
    UriInfo uriInfo;

    protected ExceptionMapperBase() {
    }

    protected ExceptionMapperBase(PostProcessorsRegistry postProcessorsRegistry) {
        this.postProcessorsRegistry = postProcessorsRegistry;
    }

    @Override
    public final Response toResponse(E exception) {
        Objects.requireNonNull(postProcessorsRegistry,
                "PostProcessorsRegistry not injected — mapper must be instantiated via CDI");
        HttpProblem problem = toProblem(exception);
        ProblemContext context = ProblemContext.of(exception, uriInfo);
        HttpProblem finalProblem = postProcessorsRegistry.applyPostProcessing(problem, context);
        return finalProblem.toResponse();
    }

    protected abstract HttpProblem toProblem(E exception);

}
