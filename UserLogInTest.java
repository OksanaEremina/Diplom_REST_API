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

public class UserLogInTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check correct user log in")
    public void correctLogInUser() {
        File jsonCreateData = new File("src/test/resources/createUserCorrectData.json");
        UserClient client = new UserClient();
        // create user
        Response response = client.getCreateUserResponseCorrect(jsonCreateData);
        token = client.parseAccessTokenFromResponse(response);
        response
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        File jsonLogInData = new File("src/test/resources/logInUserCorrectData.json");
        client.getLogInUserResponseWhenCorrectLogIn(jsonLogInData)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check that attempt to log in with wrong data follows error")
    public void attemptToLogInWithWrongData() {
        File jsonLogInData = new File("src/test/resources/logInUserCorrectData.json");
        UserClient client = new UserClient();
        client.getLogInUserResponseWhenNotCorrectLogIn(jsonLogInData)
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
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
