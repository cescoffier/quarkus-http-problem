package io.quarkiverse.httpproblem.jsonb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import io.quarkiverse.httpproblem.postprocessing.PostProcessorsRegistry;
import io.quarkiverse.httpproblem.postprocessing.ProblemDefaultsProvider;
import io.quarkiverse.httpproblem.postprocessing.ProblemLogger;

class RestEasyClassicJsonbExceptionMapperTest {

    PostProcessorsRegistry registry = new PostProcessorsRegistry(
            List.of(new ProblemLogger(), new ProblemDefaultsProvider()));
    RestEasyClassicJsonbExceptionMapper mapper = new RestEasyClassicJsonbExceptionMapper(registry);

    @Test
    void processingExceptionShouldProduceHttp500() {
        ProcessingException exception = new ProcessingException("Something is wrong");

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    void processingExceptionWithJsonbExceptionCauseShouldProduceHttp400() {
        ProcessingException exception = new ProcessingException(new JsonbException("Something is wrong"));

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(400);
    }
}
