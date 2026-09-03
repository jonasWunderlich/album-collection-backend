package com.hss.years;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class YearsAndDecadesServiceTest {

    @Test
    void testLoadsReleaseYears() {
        ObjectMapper objectMapper = new ObjectMapper();
        YearsAndDecadesService service = new YearsAndDecadesService(objectMapper, Optional.empty());
        YearsAndDecadesResponse response = service.getYearsAndDecades();

        assertNotNull(response);
        assertNotNull(response.releaseYears);
        assertNotNull(response.decades);
        assertFalse(response.releaseYears.isEmpty());
        assertFalse(response.decades.isEmpty());
        assertTrue(response.releaseYears.contains("1920"));
        assertTrue(response.decades.contains("1920"));
    }
}
