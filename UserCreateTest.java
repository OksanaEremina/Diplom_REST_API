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

public class UserCreateTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check correct creation of user")
    public void correctCreateUser() {
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
    }

    @Test
    @DisplayName("Check that attempt to create user that already exists follows error")
    public void attemptToCreateExistingUser() {
        File jsonCreateData = new File("src/test/resources/createUserCorrectData.json");
        UserClient client = new UserClient();
        Response response = client.getCreateUserResponseCorrect(jsonCreateData);
        token = client.parseAccessTokenFromResponse(response);
        response
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        client.getCreateUserResponseWhenTryToCreateExistingUser(jsonCreateData)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check that attempt to create user without email follows error")
    public void attemptToCreateUserWithoutEmail() {
        File jsonCreateData = new File("src/test/resources/createUserWithoutEmailData.json");
        UserClient client = new UserClient();
        client.getCreateUserResponseWhenTryToCreateUserWithoutEmail(jsonCreateData)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check that attempt to create user without name follows error")
    public void attemptToCreateUserWithoutName() {
        File jsonCreateData = new File("src/test/resources/createUserWithoutNameData.json");
        UserClient client = new UserClient();
        client.getCreateUserResponseWhenTryToCreateUserWithoutName(jsonCreateData)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check that attempt to create user without password follows error")
    public void attemptToCreateUserWithoutPassword() {
        File jsonCreateData = new File("src/test/resources/createUserWithoutPasswordData.json");
        UserClient client = new UserClient();
        client.getCreateUserResponseWhenTryToCreateUserWithoutPassword(jsonCreateData)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
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
