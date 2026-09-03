package com.hss.years;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 ** Loads library.json on startup and provides filtering, sorting and pagination.
 */
@ApplicationScoped
public class YearsAndDecadesService {
    
    private final List<String> releaseYears;
    private final List<String> releaseDecades;

    @Inject
    public YearsAndDecadesService(
            ObjectMapper objectMapper,
            @ConfigProperty(name = "releaseYears.json.path") Optional<String> externalYearsPath) {

        try {
            YearsAndDecades loaded;
            if (externalYearsPath.isPresent() && !externalYearsPath.get().isBlank()) {
                loaded = objectMapper.readValue(new File(externalYearsPath.get()), YearsAndDecades.class);
            } else {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("releaseYears.json")) {
                    if (is == null) {
                        throw new IllegalStateException("releaseYears.json not found on classpath. " +
                                "Set 'releaseYears.json.path' in application.properties to point to the file.");
                    }
                    loaded = objectMapper.readValue(is, YearsAndDecades.class);
                }
            }
            this.releaseYears = loaded.releaseYears() != null ? List.copyOf(loaded.releaseYears()) : Collections.emptyList();
            this.releaseDecades = loaded.decades() != null ? List.copyOf(loaded.decades()) : Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load releaseYears.json", e);
        }
    }

    /**
     * For the navigation get years and decades.
     */
    public YearsAndDecadesResponse getYearsAndDecades() {
        return new YearsAndDecadesResponse(releaseYears, releaseDecades);
    }
}
