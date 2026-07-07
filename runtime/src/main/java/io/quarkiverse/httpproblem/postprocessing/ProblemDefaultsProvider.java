package io.quarkiverse.httpproblem.postprocessing;

import jakarta.inject.Singleton;

import io.quarkiverse.httpproblem.HttpProblem;
import io.quarkiverse.httpproblem.InstanceUtils;

@Singleton
public class ProblemDefaultsProvider implements ProblemPostProcessor {

    @Override
    public int priority() {
        return 99;
    }

    @Override
    public HttpProblem apply(HttpProblem problem, ProblemContext context) {
        if (problem.getInstance() != null) {
            return problem;
        }

        return HttpProblem.builder(problem)
                .withInstance(InstanceUtils.pathToInstance(context.path))
                .build();
    }

}
