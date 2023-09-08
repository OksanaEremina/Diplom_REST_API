package api.client;

import java.io.File;

import api.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String USER_URI_SUBPATH = "/api/auth/user";
    private static final String USER_REGISTER_URI_SUBPATH = "/api/auth/register";
    private static final String USER_LOGIN_URI_SUBPATH = "/api/auth/login";

    private Response getCreateUserResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(USER_REGISTER_URI_SUBPATH);
    }

    @Step("Get response for correct user create")
    public Response getCreateUserResponseCorrect(File body) {
        return getCreateUserResponse(body);
    }

    @Step("Get response for incorrect user create, when try to create existing user")
    public Response getCreateUserResponseWhenTryToCreateExistingUser(File body) {
        return getCreateUserResponse(body);
    }

    @Step("Get response for incorrect user create, when try to create user without email")
    public Response getCreateUserResponseWhenTryToCreateUserWithoutEmail(File body) {
        return getCreateUserResponse(body);
    }

    @Step("Get response for incorrect user create, when try to create user without name")
    public Response getCreateUserResponseWhenTryToCreateUserWithoutName(File body) {
        return getCreateUserResponse(body);
    }

    @Step("Get response for incorrect user create, when try to create user without password")
    public Response getCreateUserResponseWhenTryToCreateUserWithoutPassword(File body) {
        return getCreateUserResponse(body);
    }

    private Response getLogInUserResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(USER_LOGIN_URI_SUBPATH);
    }

    @Step("Get response for correct user log in")
    public Response getLogInUserResponseWhenCorrectLogIn(File body) {
        return getLogInUserResponse(body);
    }

    @Step("Get response for not correct user log in")
    public Response getLogInUserResponseWhenNotCorrectLogIn(File body) {
        return getLogInUserResponse(body);
    }

    private Response getUpdateUserResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .patch(USER_URI_SUBPATH);
    }

    private Response getUpdateUserResponse(File body, String token) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(body)
                .when()
                .patch(USER_URI_SUBPATH);
    }

    @Step("Get response for update user without authorization")
    public Response getUpdateUserResponseWithoutAuthorization(File body) {
        return getUpdateUserResponse(body);
    }

    @Step("Get response for update user's email field")
    public Response getUpdateUserResponseForUpdateEmail(File body, String token) {
        return getUpdateUserResponse(body, token);
    }

    @Step("Get response for update user's name field")
    public Response getUpdateUserResponseForUpdateName(File body, String token) {
        return getUpdateUserResponse(body, token);
    }

    @Step("Get response for update user's password field")
    public Response getUpdateUserResponseForUpdatePassword(File body, String token) {
        return getUpdateUserResponse(body, token);
    }

    @Step("Parse access token from response")
    public String parseAccessTokenFromResponse(Response response) {
        User user = response.body().as(User.class);
        return user.getAccessToken();
    }

    private Response getDeleteUserResponse(String token) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .delete(USER_URI_SUBPATH);
    }

    @Step("Get response for delete user request, when correct user deletion")
    public Response getDeleteUserResponseWhenCorrectDeletion(String token) {
        return getDeleteUserResponse(token);
    }
}
