import java.io.File;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

import api.client.UserClient;

public class UserUpdateTest {
    private String token;
    private UserClient client;

    public void createUserAndCheckCorrectCreation() {
        File jsonCreateData = new File("src/test/resources/createUserCorrectData.json");
        // create user
        Response response = client.getCreateUserResponseCorrect(jsonCreateData);
        token = client.parseAccessTokenFromResponse(response);
        response
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Before
    public void setUp() {
        client = new UserClient();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        createUserAndCheckCorrectCreation();
    }

    @Test
    @DisplayName("Check that when try to update user without authorize then error")
    public void attemptToUpdateUserWithoutAuthorization() {
        File jsonUpdateData = new File("src/test/resources/updateUserEmailData.json");
        client.getUpdateUserResponseWithoutAuthorization(jsonUpdateData)
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Correct update user's email field")
    public void correctUpdateUsersEmailField() {
        File jsonUpdateData = new File("src/test/resources/updateUserEmailData.json");
        client.getUpdateUserResponseForUpdateEmail(jsonUpdateData, token)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo("kate-test-asd@ya.ru"));
    }

    @Test
    @DisplayName("Correct update user's name field")
    public void correctUpdateUsersNameField() {
        File jsonUpdateData = new File("src/test/resources/updateUserNameData.json");
        client.getUpdateUserResponseForUpdateName(jsonUpdateData, token)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo("test"));
    }

    @Test
    @DisplayName("Correct update user's password field")
    public void correctUpdateUsersPasswordField() {
        File jsonUpdateData = new File("src/test/resources/updateUserPasswordData.json");
        client.getUpdateUserResponseForUpdatePassword(jsonUpdateData, token)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @After
    public void tearDown() throws Exception {
        if (token != null && !token.isEmpty()) {
            // cleanup steps
            UserClient client = new UserClient();
            client.getDeleteUserResponseWhenCorrectDeletion(token)
                    .then()
                    .statusCode(HTTP_ACCEPTED);
        }
    }
}
