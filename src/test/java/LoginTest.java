import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.LoginPage;
import pageObjects.MainPage;
import pageObjects.RecoveryPasswordPage;
import pageObjects.RegistrationPage;

import java.time.Duration;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    WebDriver webDriver;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        User user = new User("Nezuko@gmail.com", "123456", "Nezuko");
        Response createResponse = UserClient.createUser(user);
        accessToken = createResponse.path("accessToken");
    }

    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    public void checkLoginButton() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.open();
        mainPage.waitMainPage();
        mainPage.clickLoginButton();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.inputEmail("Nezuko@gmail.com");
        loginPage.inputPassword("123456");
        loginPage.clickEnter();
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке в корзине", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    public void checkLoginAccountButton() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.open();
        mainPage.waitMainPage();
        mainPage.clickLoginAccountButton();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.inputEmail("Nezuko@gmail.com");
        loginPage.inputPassword("123456");
        loginPage.clickEnter();
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void checkLoginButtonRegistrationPage() {
        RegistrationPage registrationPage = new RegistrationPage(webDriver);
        registrationPage.open();
        registrationPage.clickEnter();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.inputEmail("Nezuko@gmail.com");
        loginPage.inputPassword("123456");
        loginPage.clickEnter();
        MainPage mainPage = new MainPage(webDriver);
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public  void checkLoginButtonRecoveryPasswordPage() {
        RecoveryPasswordPage recoveryPasswordPage = new RecoveryPasswordPage(webDriver);
        recoveryPasswordPage.open();
        recoveryPasswordPage.clickEnter();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.inputEmail("Nezuko@gmail.com");
        loginPage.inputPassword("123456");
        loginPage.clickEnter();
        MainPage mainPage = new MainPage(webDriver);
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
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
