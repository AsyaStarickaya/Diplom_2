import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CreateUserJson;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


@RunWith(Parameterized.class)
public class CreateUserWithoutFieldParametrizedTest {
    private final CreateUserJson finalUserJsonWithoutField;

    public CreateUserWithoutFieldParametrizedTest(CreateUserJson jsonForCreatingWithoutField) {
        this.finalUserJsonWithoutField = jsonForCreatingWithoutField;
    }

    @Parameterized.Parameters
    public static Object[] getSex() {
        CreateUserJson jsonForCreatingWithoutEmail = new CreateUserJson(null, ApiConstants.USER_PASSWORD, ApiConstants.USER_NAME);
        CreateUserJson jsonForCreatingWithoutPassword = new CreateUserJson(ApiConstants.USER_EMAIL, null, ApiConstants.USER_NAME);
        CreateUserJson jsonForCreatingWithoutName = new CreateUserJson(ApiConstants.USER_EMAIL, ApiConstants.USER_PASSWORD, null);
        return new Object[][]{
                {jsonForCreatingWithoutEmail},
                {jsonForCreatingWithoutPassword},
                {jsonForCreatingWithoutName},
        };
    }

    @Test
    public void haveManeShouldBeEqual(){
        UserApi.sendPostRequest(finalUserJsonWithoutField, ApiConstants.URL_FOR_CREATING)
                .statusCode(SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
