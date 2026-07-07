package io.quarkiverse.httpproblem.validation;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.Priorities;

import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;

/**
 * Exception Mapper for generic ValidationException from Bean Validation API.
 * Unlike ConstraintViolationException these are not thrown if the input fails validation,
 * but are instead thrown on invalid use of the API.
 */
@Priority(Priorities.USER)
public final class ValidationExceptionMapper extends ExceptionMapperBase<ValidationException> {

    public ValidationExceptionMapper() {
    }

    @Inject
    public ValidationExceptionMapper(PostProcessorsRegistry postProcessorsRegistry) {
        super(postProcessorsRegistry);
    }

    @Override
    protected HttpProblem toProblem(ValidationException exception) {
        return HttpProblem.valueOf(INTERNAL_SERVER_ERROR);
    }
}
