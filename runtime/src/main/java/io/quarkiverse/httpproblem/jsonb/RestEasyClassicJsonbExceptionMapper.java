package io.quarkiverse.httpproblem.jsonb;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ProcessingException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;

@Priority(Priorities.USER)
public final class RestEasyClassicJsonbExceptionMapper extends ExceptionMapperBase<ProcessingException> {

    static final String SANITIZED_DETAIL = "Malformed request body";

    private final boolean includeDetails;

    public RestEasyClassicJsonbExceptionMapper() {
        this.includeDetails = false;
    }

    @Inject
    public RestEasyClassicJsonbExceptionMapper(PostProcessorsRegistry postProcessorsRegistry,
            @ConfigProperty(name = "quarkus.http-problem.include-details", defaultValue = "false") boolean includeDetails) {
        super(postProcessorsRegistry);
        this.includeDetails = includeDetails;
    }

    /**
     * Unfortunately Quarkus+JsonB throws ProcessingException, not JsonbException in case of malformed payload body, so `cause`
     * needs to be checked explicitly.
     *
     * For native mode compatibility instanceof operator is not used to check cause type.
     */
    @Override
    protected HttpProblem toProblem(ProcessingException exception) {
        if (exception.getCause() != null
                && exception.getCause().getClass().getName().equals("jakarta.json.bind.JsonbException")) {
            String detail = includeDetails
                    ? exception.getCause().getMessage()
                    : SANITIZED_DETAIL;
            return HttpProblem.valueOf(BAD_REQUEST, detail);
        } else {
            return HttpProblem.valueOf(INTERNAL_SERVER_ERROR);
        }
    }
}
