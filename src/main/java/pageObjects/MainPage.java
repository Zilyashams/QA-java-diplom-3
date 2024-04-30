package pageObjects;

import io.qameta.allure.Step;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;

public class MainPage {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private final By loginButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By accountButton = By.xpath(".//p[text()='Личный Кабинет']");
    private final By basketButton = By.xpath(".//div[starts-with(@class,'BurgerConstructor_basket__container__2fUl3 mt-10')]/button");
    private final By ingredientsTitles = By.xpath(".//div[starts-with(@class, 'BurgerIngredients_ingredients__menuContainer__Xu3Mo')]/h2");
    private final By bunsButton = By.xpath("//*[text()= 'Булки']");
    private final By sauceButton = By.xpath("//*[text()= 'Соусы']");
    private final By fillingButton = By.xpath("//*[text()= 'Начинки']");
    private static final By currentMenu = By.xpath("//div[contains(@class,'tab_tab__1SPyG tab_tab_type_current__2BEPc')]");

    private final WebDriver webDriver;
    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private void waitScrolled(int n) {
        new WebDriverWait(webDriver, Duration.ofSeconds(50))
                .until(webDriver -> {
                            return webDriver.findElements(ingredientsTitles).get(n).getLocation().getY() == 243;
                        }
                );
    }

    @Step("Открытие главной страницы")
    public void open() {
        webDriver.get(BASE_URL);
    }

    @Step("Тап по кнопке «Войти в аккаунт»")
    public void clickLoginButton() {
        webDriver.findElement(loginButton).click();
    }

    @Step("Ожидание загрузки страницы")
    public void waitMainPage() {
        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(loginButton));
    }

    @Step("Получение текста на кнопке в Корзине")
    public String getBasketButtonText() {
        return webDriver.findElement(basketButton).getText();
    }

    @Step("Тап по кнопке «Личный кабинет»")
    public void clickLoginAccountButton() {
        webDriver.findElement(accountButton).click();
    }

    @Step("Тап по вкладке «Булки»")
    public void clickBunsButton() {
        webDriver.findElement(bunsButton).click();
        waitScrolled(0);
    }

    @Step("Тап по вкладке «Соусы»")
    public void clickSauceButton() {
        webDriver.findElement(sauceButton).click();
        waitScrolled(1);
    }

    @Step("Тап по вкладке «Начинки»")
    public void clickFillingButton() {
        webDriver.findElement(fillingButton).click();
        waitScrolled(2);
    }

    @Step("Получить выбранный раздел")
    public String getTextFromSelectedMenu() {
        return webDriver.findElement(currentMenu).getText();
    }
}
