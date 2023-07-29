import org.junit.After;
import org.junit.Test;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserInfoTest {
    CreateUserJson jsonForCreating = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
    CreateUserJson jsonForNewInfo = new CreateUserJson(ApiConstants.USER_NEW_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NEW_NAME);

    String bearerToken;
    @Test
    public void changeInfoForAuthUser() {
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        UserApi.sendPatchRequest(jsonForNewInfo, ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7))
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", equalTo(ApiConstants.USER_NEW_EMAIL))
                .assertThat().body("user.name", equalTo(ApiConstants.USER_NEW_NAME));
    }

    @Test
    public void changeInfoForNotAuthUser(){
        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        UserApi.sendPatchRequest(jsonForNewInfo, ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO)
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"));

    }

    @After
    public void tearDown() {
        UserApi.deleteUser(ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7));
    }
}
