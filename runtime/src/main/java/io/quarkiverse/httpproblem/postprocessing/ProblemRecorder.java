package io.quarkiverse.httpproblem.postprocessing;

import java.util.Set;

import io.quarkiverse.httpproblem.validation.ConstraintViolationConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ProblemRecorder {

    public RuntimeValue<MdcPropertiesInjector> createMdcInjector(Set<String> properties) {
        return new RuntimeValue<>(new MdcPropertiesInjector(properties));
    }

    public RuntimeValue<ConstraintViolationConfig> createConstraintViolationConfig(int status, String title) {
        return new RuntimeValue<>(new ConstraintViolationConfig(status, title));
    }

}
