import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.RegistrationPage;

import java.time.Duration;

import static api.UserCreds.from;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class RegistrationTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    WebDriver webDriver;
    private String accessToken;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    public void checkRegistration() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);
        registrationPage.open();
        registrationPage.inputName("Nezuko");
        registrationPage.inputEmail("Nezuko@gmail.com");
        registrationPage.inputPassword("123456");
        registrationPage.clickRegister();
        RestAssured.baseURI = BASE_URL;
        User user = new User("Nezuko@gmail.com", "123456", "Nezuko");
        Response loginResponse = UserClient.loginUser(from(user));
        accessToken = loginResponse.path("accessToken");
        Assert.assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
    }

    @Test
    @DisplayName("Проверка ошибку для некорректного пароля. Минимальный пароль — шесть символов")
    public void checkRegistrationWithWrongPassword() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);
        registrationPage.open();
        registrationPage.inputName("Nezuko");
        registrationPage.inputEmail("Nezuko@gmail.com");
        registrationPage.inputPassword("12345");
        registrationPage.clickRegister();
        Assert.assertTrue("Некорректный пароль", registrationPage.passwordError());
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        webDriver.quit();
        if (accessToken != null) {
            UserClient.deleteUser(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"))
                    .and()
                    .statusCode(SC_ACCEPTED);
        }
    }
}
