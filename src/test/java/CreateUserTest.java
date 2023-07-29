import org.junit.After;
import org.junit.Test;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    CreateUserJson jsonForCreating = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);

    String bearerToken;
    @Test
    public void createUser() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    public void tryToCreateTwoSameUsers() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("User already exists"));
    }


    @After
    public void tearDown() {
        System.out.println(bearerToken.substring(7));
        UserApi.deleteUser(ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7));
    }
}

