import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.LoginPage;
import pageObjects.MainPage;
import pageObjects.RegistrationPage;
import pageObjects.UserProfilePage;

import java.time.Duration;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.equalTo;

public class ProfilePageTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    WebDriver webDriver;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        User user = new User("Nezuko@gmail.com", "123456", "Nezuko");
        Response createResponse = UserClient.createUser(user);
        accessToken = createResponse.path("accessToken");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        MainPage mainPage = new MainPage(webDriver);
        mainPage.open();
        mainPage.clickLoginAccountButton();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.inputEmail("Nezuko@gmail.com");
        loginPage.inputPassword("123456");
        loginPage.clickEnter();
    }

    @Test
    @DisplayName("Переход в личный кабинет по клику на «Личный кабинет»")
    public void checkLoginAccountButton() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickLoginAccountButton();
        UserProfilePage userProfilePage = new UserProfilePage(webDriver);
        MatcherAssert.assertThat("Ожидается надпись «Профиль» на странице личного кабинета", userProfilePage.getProfileText(), equalTo("Профиль"));
    }

    @Test
    @DisplayName("Переход из личного кабинета по клику на «Конструктор»")
    public void checkTapToConstructor() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickLoginAccountButton();
        UserProfilePage userProfilePage = new UserProfilePage(webDriver);
        userProfilePage.clickConstructorButton();
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке в корзине", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
    }

    @Test
    @DisplayName("Переход из личного кабинета на логотип Stellar Burgers")
    public void checkTapToLogo() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickLoginAccountButton();
        UserProfilePage userProfilePage = new UserProfilePage(webDriver);
        userProfilePage.clicklogoButton();
        MatcherAssert.assertThat("Надпись 'Оформить заказ' на кнопке в корзине", mainPage.getBasketButtonText(), equalTo("Оформить заказ"));
    }

    @Test
    @DisplayName("Выход из аккаунта по кнопке «Выйти» в личном кабинете")
    public void checkTapToExit() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickLoginAccountButton();
        UserProfilePage userProfilePage = new UserProfilePage(webDriver);
        userProfilePage.clickExitButton();
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.waitLoginPage();
        Assert.assertTrue(loginPage.enterButton());
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
