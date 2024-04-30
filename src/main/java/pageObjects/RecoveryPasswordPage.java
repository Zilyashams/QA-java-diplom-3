package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class RecoveryPasswordPage {
    public static final String URL = "https://stellarburgers.nomoreparties.site/forgot-password";
    private final WebDriver webDriver;
    private final By enterButton = By.xpath(".//a[text()='Войти']");

    public RecoveryPasswordPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Step("Открытие страницы восстановления пароля")
    public void open() {
        webDriver.get(URL);
    }

    @Step("Тап по кнопке «Войти» на странице восстановления пароля")
    public void clickEnter() {
        webDriver.findElement(enterButton).click();
    }
}