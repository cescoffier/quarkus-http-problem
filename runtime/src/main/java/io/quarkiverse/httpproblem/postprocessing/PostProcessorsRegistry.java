package io.quarkiverse.httpproblem.postprocessing;

import io.quarkiverse.httpproblem.HttpProblem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Container for prioritised list of Problem post-processors.
 * Collects all CDI beans implementing ProblemPostProcessor and sorts them by priority at startup.
 */
@ApplicationScoped
public class PostProcessorsRegistry {

    private List<ProblemPostProcessor> processors;

    @Inject
    PostProcessorsRegistry(Instance<ProblemPostProcessor> processorInstances) {
        this.processors = processorInstances.stream()
                .sorted(ProblemPostProcessor.DEFAULT_ORDERING)
                .toList();
    }

    public PostProcessorsRegistry(List<ProblemPostProcessor> processors) {
        this.processors = processors.stream()
                .sorted(ProblemPostProcessor.DEFAULT_ORDERING)
                .toList();
    }

    public HttpProblem applyPostProcessing(HttpProblem problem, ProblemContext context) {
        HttpProblem finalProblem = problem;
        for (ProblemPostProcessor processor : processors) {
            finalProblem = processor.apply(finalProblem, context);
        }
        return finalProblem;
    }

}
