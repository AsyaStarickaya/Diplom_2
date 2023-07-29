import org.junit.After;
import org.junit.Test;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    CreateUserJson jsonForCreating = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
    CreateUserJson jsonWithWrongLogin = new CreateUserJson(ApiConstants.USER_WRONG_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
    CreateUserJson jsonWithWrongPassword = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_WRONG_PASSWORD, ApiConstants.USER_NAME);

    String bearerToken;

    @Test
    public void loginForExistedUser() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    public void loginUserWithWrongLogin() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        UserApi.sendPostRequest(jsonWithWrongLogin, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"));


        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    public void loginUserWithWrongPassword() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        UserApi.sendPostRequest(jsonWithWrongPassword, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"));


        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @After
    public void tearDown() {
        System.out.println(bearerToken.substring(7));
        UserApi.deleteUser(ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7));
    }
}
