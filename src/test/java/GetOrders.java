import org.junit.After;
import org.junit.Test;
import pojo.CreateOrderJson;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class GetOrders {
    CreateUserJson jsonForCreating = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
    CreateOrderJson jsonForCreatingOrder = new CreateOrderJson(ApiConstants.INGREDIENT1, ApiConstants.INGREDIENT2);

    String bearerToken;

    @Test
    public void getOrderForAuthUser() {
        //create user
        UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        //login user
        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        //create order
        UserApi.sendPostRequest(jsonForCreatingOrder, ApiConstants.URL_FOR_CREATION_ORDER, bearerToken.substring(7))
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        //get order
        UserApi.sendGetRequest(bearerToken.substring(7), ApiConstants.URL_FOR_GET_ORDER)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("orders.size()", equalTo(1));
    }

    @Test
    public void getOrderForNotAuthUser() {
        //create user
        bearerToken = UserApi.sendPostRequest(jsonForCreating, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        //create order
        UserApi.sendPostRequest(jsonForCreatingOrder, ApiConstants.URL_FOR_CREATION_ORDER, bearerToken.substring(7))
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        //get order
        UserApi.sendGetRequest(ApiConstants.URL_FOR_GET_ORDER)
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        System.out.println(bearerToken.substring(7));
        UserApi.deleteUser(ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7));
    }
}
