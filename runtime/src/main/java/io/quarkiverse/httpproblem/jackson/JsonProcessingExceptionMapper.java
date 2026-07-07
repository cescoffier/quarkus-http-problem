package io.quarkiverse.httpproblem.jackson;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;

/**
 * Mapper for Jackson payload processing exceptions.
 */
@Priority(Priorities.USER)
public final class JsonProcessingExceptionMapper extends ExceptionMapperBase<JsonProcessingException> {

    public JsonProcessingExceptionMapper() {
    }

    @Inject
    public JsonProcessingExceptionMapper(PostProcessorsRegistry postProcessorsRegistry) {
        super(postProcessorsRegistry);
    }

    @Override
    protected HttpProblem toProblem(JsonProcessingException exception) {
        return HttpProblem.valueOf(BAD_REQUEST, exception.getOriginalMessage());
    }
}
