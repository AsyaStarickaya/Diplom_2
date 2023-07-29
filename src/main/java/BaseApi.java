import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class BaseApi {
    public static ValidatableResponse sendPostRequest(Object json, String URL) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .header("Content-type", "application/json")
                .body(json)
                .log().all()
                .when()
                .post(URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse sendPostRequest(Object json, String URL, String bearerToken) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(json)
                .log().all()
                .when()
                .post(URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse sendPatchRequest(Object json, String URL, String bearerToken) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(json)
                .log().all()
                .when()
                .patch(URL)
                .then()
                .log().all();
    }
    public static ValidatableResponse sendPatchRequest(Object json, String URL) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(json)
                .log().all()
                .when()
                .patch(URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse sendGetRequest(String bearerToken, String URL) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .log().all()
                .when()
                .get(URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse sendGetRequest(String URL) {
        RestAssured.baseURI = ApiConstants.BASE_URL;

        return given()
                .headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .log().all()
                .when()
                .get(URL)
                .then()
                .log().all();
    }
}
