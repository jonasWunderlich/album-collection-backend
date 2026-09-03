package com.hss.years;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public record YearsAndDecades(
    @JsonProperty("releaseYears") List<String> releaseYears,
    @JsonProperty("decades") List<String> decades
) {}
