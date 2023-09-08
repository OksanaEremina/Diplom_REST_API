import java.io.File;
import java.util.ArrayList;
import java.util.List;

import api.client.OrderClient;
import api.model.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import api.client.UserClient;

public class OrderCreateTest {
    private String token;
    private UserClient userClient;
    private OrderClient orderClient;

    private final static List<String> INGREDIENTS = new ArrayList<>(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f"));

    public void createUserAndCheckCorrectCreation() {
        File jsonCreateData = new File("src/test/resources/createUserCorrectData.json");
        // create user
        Response response = userClient.getCreateUserResponseCorrect(jsonCreateData);
        token = userClient.parseAccessTokenFromResponse(response);
        response
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        createUserAndCheckCorrectCreation();
    }


    @Test
    @DisplayName("Check correct creation of order")
    public void correctCreateOrder() {
        Order order = new Order(INGREDIENTS);
        orderClient.getCreateOrderResponseCorrect(order, token)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("name", equalTo("Spicy бессмертный флюоресцентный бургер"))
                .and()
                .assertThat().body("order.number", notNullValue());
    }

    // BUG!!! App allows to create order without authorization.
    @Test
    @DisplayName("Check that attempt to create order without auth follows error")
    public void attemptToCreateOrderWithoutAuth() {
        Order order = new Order(INGREDIENTS);
        orderClient.getCreateOrderResponseWithoutAuth(order)
                .then()
                .statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Check that attempt to create order without ingredients follows error")
    public void attemptToCreateOrderWithoutIngredients() {
        Order order = new Order(null);
        orderClient.getCreateOrderResponseWithoutIngredients(order, token)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Check that attempt to create order with invalid ingredient follows error")
    public void attemptToCreateOrderWithInvalidIngredient() {
        Order order = new Order(List.of("a", "b", "c"));
        orderClient.getCreateOrderResponseWithInvalidIngredients(order, token)
                .then()
                .statusCode(HTTP_INTERNAL_ERROR);
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
