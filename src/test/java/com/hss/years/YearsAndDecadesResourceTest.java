package com.hss.years;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class YearsAndDecadesResourceTest {

    @Test
    void testYearsAndDecadesEndpoint() {
        given()
                .when().get("/yearsAndDecades")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("releaseYears", not(empty()))
                .body("decades", not(empty()));
    }

    @Test
    void testYearsAndDecadesWithBrowserAcceptHeader() {
        given()
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .when().get("/yearsAndDecades")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void testTrailingSlash() {
        given()
                .when().get("/yearsAndDecades/")
                .then()
                .statusCode(200);
    }
}
