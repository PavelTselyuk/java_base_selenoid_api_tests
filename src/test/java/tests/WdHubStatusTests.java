package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class WdHubStatusTests extends TestBase {
    @Test
    public void statusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
//                .get("https://user1:1234@selenoid.qa.guru/wd/hub/status") //BAD PRACTICE
                .get("https://selenoid.qa.guru/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void badCredentialsTest() {
        given()
                .log().all()
                .auth().basic("user1", "1111")
                .when()
//                .get("https://user1:1234@selenoid.qa.guru/wd/hub/status") //BAD PRACTICE
                .get("https://selenoid.qa.guru/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401)
                .body(containsString("Authorization Required"));
    }

    @Test
    public void unauthorizedStatusTest() {
        given()
                .log().all()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401)
                .body(containsString("Authorization Required"));
    }

    @Test
    public void selenoidVersionTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.qa.guru/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/wd_hub_status_response_schema.json"))
                .body("value.message", containsString("Selenoid v3.0.16 built at"));
    }

    @Test
    public void selenoidReadyTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.qa.guru/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/wd_hub_status_response_schema.json"))
                .body("value.ready", is(true));
    }
}
