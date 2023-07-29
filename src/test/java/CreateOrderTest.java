import org.junit.*;
import pojo.CreateOrderJson;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {

    CreateOrderJson jsonForCreatingOrder = new CreateOrderJson(ApiConstants.INGREDIENT1, ApiConstants.INGREDIENT2);
    CreateOrderJson jsonForCreatingOrderWithoutIngredients = new CreateOrderJson();
    CreateOrderJson jsonForCreatingOrderWithFalseIngredientsIndex = new CreateOrderJson(ApiConstants.WRONG_INDEX_INGREDIENT1, ApiConstants.WRONG_INDEX_INGREDIENT2);
    CreateUserJson jsonForCreatingUser = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
    String bearerToken;


    @Test
    public void createOrderForAuthUser() {
        //create and login user
        UserApi.sendPostRequest(jsonForCreatingUser, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));

        bearerToken = UserApi.sendPostRequest(jsonForCreatingUser, ApiConstants.URL_FOR_LOGIN)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");


        //create order
        UserApi.sendPostRequest(jsonForCreatingOrder, ApiConstants.URL_FOR_CREATION_ORDER, bearerToken.substring(7))
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name", equalTo("Люминесцентный флюоресцентный бургер"));
    }

    @Test
    public void createOrderForNotAuthUser() {
        //create user
        bearerToken = UserApi.sendPostRequest(jsonForCreatingUser, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");


        //create order
        UserApi.sendPostRequest(jsonForCreatingOrder, ApiConstants.URL_FOR_CREATION_ORDER)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name", equalTo("Люминесцентный флюоресцентный бургер"));
    }

    @Test
    public void createOrderWithoutIngredients() {
        //create user
        bearerToken = UserApi.sendPostRequest(jsonForCreatingUser, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");


        //create order
        UserApi.sendPostRequest(jsonForCreatingOrderWithoutIngredients, ApiConstants.URL_FOR_CREATION_ORDER, bearerToken.substring(7))
                .statusCode(SC_BAD_REQUEST)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithFalseIngredientsIndex() {
        //create user
        bearerToken = UserApi.sendPostRequest(jsonForCreatingUser, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .extract().path("accessToken");

        //create order
        UserApi.sendPostRequest(jsonForCreatingOrderWithFalseIngredientsIndex, ApiConstants.URL_FOR_CREATION_ORDER, bearerToken.substring(7))
                .statusCode(SC_INTERNAL_SERVER_ERROR);


    }

    @After
    public void tearDown() {
        System.out.println(bearerToken.substring(7));
        UserApi.deleteUser(ApiConstants.URL_FOR_DELETE_ACC_AND_CHANGE_INFO, bearerToken.substring(7));
    }
}
