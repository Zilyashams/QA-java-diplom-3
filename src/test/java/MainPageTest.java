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
import pageObjects.LoginPage;
import pageObjects.MainPage;

import java.time.Duration;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.equalTo;

public class MainPageTest {
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
    @DisplayName("Переход к разделу «Булки»")
    public void checkLinkToBuns() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickSauceButton();
        mainPage.clickBunsButton();
        Assert.assertEquals("Выбран некорректный раздел", "Булки", mainPage.getTextFromSelectedMenu());
    }

    @Test
    @DisplayName("Переход к разделу «Соусы»")
    public void checkLinkToSauce() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickSauceButton();
        Assert.assertEquals("Выбран некорректный раздел", "Соусы", mainPage.getTextFromSelectedMenu());
    }

    @Test
    @DisplayName("Переход к разделу «Начинки»")
    public void checkLinkToFilling() {
        MainPage mainPage = new MainPage(webDriver);
        mainPage.clickFillingButton();
        Assert.assertEquals("Выбран некорректный раздел", "Начинки", mainPage.getTextFromSelectedMenu());
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
