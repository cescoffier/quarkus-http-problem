package io.quarkiverse.httpproblem;

import java.util.Optional;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;

import org.zalando.problem.ThrowableProblem;

import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;

/**
 * Mapper for ThrowableProblem exception from Zalando Problem library.
 */
@Priority(Priorities.USER)
public final class ZalandoProblemMapper extends ExceptionMapperBase<ThrowableProblem> {

    public ZalandoProblemMapper() {
    }

    @Inject
    public ZalandoProblemMapper(PostProcessorsRegistry postProcessorsRegistry) {
        super(postProcessorsRegistry);
    }

    @Override
    protected HttpProblem toProblem(ThrowableProblem exception) {
        Response.StatusType status = Optional.ofNullable(exception.getStatus())
                .map(problemStatus -> Response.Status.fromStatusCode(problemStatus.getStatusCode()))
                .orElse(Response.Status.INTERNAL_SERVER_ERROR);

        HttpProblem.Builder builder = HttpProblem.builder()
                .withType(exception.getType())
                .withTitle(exception.getTitle())
                .withStatus(status)
                .withDetail(exception.getDetail())
                .withInstance(exception.getInstance());
        exception.getParameters().forEach(builder::with);
        return builder.build();
    }

}
