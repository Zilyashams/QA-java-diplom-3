package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {
    public static String PAGE_URL = "https://stellarburgers.nomoreparties.site/register";
    private final By nameField = By.xpath(".//label[text()='Имя']/../input");
    private final By emailField = By.xpath(".//label[text()='Email']/../input");
    private final By passwordField = By.xpath(".//label[text()='Пароль']/../input");
    private final By registrationButton = By.xpath(".//button[text()='Зарегистрироваться']");
    private final By enterButton = By.xpath(".//a[text()='Войти']");
    private final By passwordError = By.xpath(".//p[text()='Некорректный пароль']");

    private final WebDriver webDriver;

    public RegistrationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Step("Открытие страницы Регистрации")
    public void open() {
        webDriver.get(PAGE_URL);
    }

    @Step("Заполнение поля «Email»")
    public void inputEmail(String email) {
        webDriver.findElement(emailField).sendKeys(email);
    }

    @Step("Заполнение поля «Имя»")
    public void inputName(String name) {
        webDriver.findElement(nameField).sendKeys(name);
    }

    @Step("Заполнение поля «Пароль»")
    public void inputPassword(String password) {
        webDriver.findElement(passwordField).sendKeys(password);
    }

    @Step("Тап по кнопке «Зарегистрироваться»")
    public void clickRegister() {
        webDriver.findElement(registrationButton).click();
    }

    @Step("Тап по кнопке «Войти»")
    public void clickEnter() {
        webDriver.findElement(enterButton).click();
    }

    @Step("Текст «Некорректный пароль»")
    public boolean passwordError() {
        return webDriver.findElement(passwordError).isDisplayed();
    }
}
