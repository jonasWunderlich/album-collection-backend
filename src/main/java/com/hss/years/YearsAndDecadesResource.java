package com.hss.years;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST resource for album queries.
 */
@Path("/yearsAndDecades")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "YearsAndDecades", description = "Operations on the album collection")
public class YearsAndDecadesResource {

    @Inject
    YearsAndDecadesService yearsAndDecadesService;

    @GET
    @Operation(
            summary = "Get all found years and corresponding decades",
            description = "Returns a two lists of years and decades."
    )
    public YearsAndDecadesResponse getYears(
    ) {
        return yearsAndDecadesService.getYearsAndDecades();
    }
}
