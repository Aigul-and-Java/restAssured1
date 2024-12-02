package api;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.openqa.selenium.remote.http.HttpHeader.ContentType;


public class ReqresTest {
    private static final String BASE_URL = "https://reqres.in/";

    @Test
    public void getUsersTest() {
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data",UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

       Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> emails = users.stream().map(userData -> userData.getAvatar()).collect(Collectors.toList());
        List<String> id = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i < id.size(); i++) {
            Assert.assertTrue(emails.get(i).contains(id.get(i)));
        }

    }

    @Test
    public void succesRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec());
    Integer id = 4;
    String token= "QpwL5tke4Pnpja7X4";
    Register user = new Register("eve.holt@reqres.in", "pistol");
    SuccessReg successReg = given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            .extract().as(SuccessReg.class);
        Assert.assertNotNull(successReg.getId());
        Assert.assertNotNull(successReg.getToken());

        Assert.assertEquals(id, successReg.getId());
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    public void unsuccessTest(){
        Specifications.installSpecification(Specifications.requestSpec(BASE_URL), Specifications.responseSpec400());
        Register user = new Register("sydney@fife", "");
        Unsuccess unsuccessReg = given()
                .body(user)
                .post("api/register")
                .then().log().all()
                .extract().as(Unsuccess.class);
        Assert.assertEquals("Missing password", unsuccessReg.getError());
    }
}
