package testBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By emailField = By.cssSelector("#mat-input-0");
    private By passwordField = By.cssSelector("#mat-input-1");
    private By loginButton = By.cssSelector("button[type='submit']");

    // Actions with 5-second waits
    public void enterEmail(String email) throws InterruptedException {
        driver.findElement(emailField).sendKeys(email);
        Thread.sleep(5000);  // wait after entering email
    }

    public void enterPassword(String password) throws InterruptedException {
        driver.findElement(passwordField).sendKeys(password);
        Thread.sleep(5000); // wait after entering password
    }

    public void clickLogin() throws InterruptedException {
        driver.findElement(loginButton).click();
        Thread.sleep(5000); // wait after clicking login
    }

    // Complete login flow
    public void loginToAdmin(String email, String password) throws InterruptedException {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    // Success = redirected to home URL
    public boolean isLoginSuccessful() {
        return driver.getCurrentUrl().contains("/Administrator/Home");
    }
}
