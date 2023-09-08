import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersRecieveTest {
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
    @DisplayName("Check correct recieve orders of user")
    public void correctRecieveOrdersOfUser() {
        Order order = new Order(INGREDIENTS);
        orderClient.getCreateOrderResponseCorrect(order, token)
                .then()
                .statusCode(HTTP_OK);

        orderClient.getRecieveOrdersOfUserResponseCorrect(token)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("orders", notNullValue());
    }

    @Test
    @DisplayName("Check that attempt to recieve orders without auth follows error")
    public void attemptToRecieveOrdersOfUserWithoutAuth() {
        Order order = new Order(INGREDIENTS);
        orderClient.getCreateOrderResponseCorrect(order, token)
                .then()
                .statusCode(HTTP_OK);

        orderClient.getRecieveOrdersOfUserResponseWithoutAuth()
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
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
