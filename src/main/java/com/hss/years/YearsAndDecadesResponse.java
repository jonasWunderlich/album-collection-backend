package com.hss.years;

import java.util.List;

/**
 * Years and Decades response.
 */
public class YearsAndDecadesResponse {

    public List<String> releaseYears;
    public List<String> decades;

    public YearsAndDecadesResponse(List<String> releaseYears, List<String> decades) {
        this.releaseYears = releaseYears;
        this.decades = decades;
    }
}
