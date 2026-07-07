package io.quarkiverse.httpproblem.deployment;

import java.util.Map;
import java.util.Set;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "quarkus.http-problem")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface ProblemBuildConfig {

    /**
     * MDC properties that should be included in problem responses.
     */
    @WithDefault("uuid")
    Set<String> includeMdcProperties();

    /**
     * OpenApi related configuration
     */
    @WithName("openapi")
    OpenApiConfig openapi();

    interface OpenApiConfig {

        /**
         * Which schema should be used by default for problem responses
         */
        @WithName("default-schema")
        @WithDefault("HttpProblem")
        String defaultSchema();

        /**
         * Which schema should be used by default for validation problem responses
         */
        @WithName("validation-problem-schema")
        @WithDefault("HttpValidationProblem")
        String validationProblemSchema();
    }

    /**
     * Per-mapper configuration, keyed by exception simple name in kebab-case
     * (e.g. {@code web-application-exception}, {@code not-found-exception}).
     * <p>
     * Use this to disable specific built-in exception mappers:
     *
     * <pre>
     * quarkus.http-problem.mapper.not-found-exception.enabled=false
     * </pre>
     */
    @WithName("mapper")
    Map<String, MapperConfig> mapper();

    interface MapperConfig {
        /**
         * Whether the mapper for this exception type should be registered.
         */
        @WithDefault("true")
        boolean enabled();
    }

    /**
     * Config for OpenApi schema of HttpValidationProblem
     */
    @WithName("constraint-violation")
    ConstraintViolationMapperConfig constraintViolation();

    interface ConstraintViolationMapperConfig {
        /**
         * Response status code when ConstraintViolationException is thrown.
         */
        @WithDefault("400")
        int status();

        /**
         * Response title when ConstraintViolationException is thrown.
         */
        @WithDefault("Bad Request")
        String title();

        /**
         * OpenApi description for ConstraintViolationExceptions.
         */
        @WithDefault("Bad request: server would not process the request due to something the server considered to be a client error")
        String description();
    }
}
