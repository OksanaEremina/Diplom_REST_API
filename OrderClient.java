package api.client;


import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_URI_SUBPATH = "/api/orders";

    private Response getCreateOrderResponse(Order order, String authToken) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", authToken)
                .and()
                .body(order)
                .when()
                .post(ORDERS_URI_SUBPATH);
    }

    private Response getCreateOrderResponse(Order order) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDERS_URI_SUBPATH);
    }

    @Step("Get response for correct order create")
    public Response getCreateOrderResponseCorrect(Order order, String authToken) {
        return getCreateOrderResponse(order, authToken);
    }

    @Step("Get response for not correct order create, without auth")
    public Response getCreateOrderResponseWithoutAuth(Order order) {
        return getCreateOrderResponse(order);
    }

    @Step("Get response for not correct order create, without ingredients")
    public Response getCreateOrderResponseWithoutIngredients(Order order, String authToken) {
        return getCreateOrderResponse(order, authToken);
    }

    @Step("Get response for not correct order create, with invalid ingredients")
    public Response getCreateOrderResponseWithInvalidIngredients(Order order, String authToken) {
        return getCreateOrderResponse(order, authToken);
    }

    private Response getRecieveOrdersOfUserResponse(String authToken) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", authToken)
                .when()
                .get(ORDERS_URI_SUBPATH);
    }

    private Response getRecieveOrdersOfUserResponse() {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS_URI_SUBPATH);
    }

    @Step("Get response for recieve orders of user, without auth")
    public Response getRecieveOrdersOfUserResponseWithoutAuth() {
        return getRecieveOrdersOfUserResponse();
    }

    @Step("Get response for recieve orders of user, with auth")
    public Response getRecieveOrdersOfUserResponseCorrect(String authToken) {
        return getRecieveOrdersOfUserResponse(authToken);
    }

}
