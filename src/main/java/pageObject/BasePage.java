package pageObject;


import java.io.File;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private final int DEFAULT_TIMEOUT = 20;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        waitForClickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public void jsClick(By locator) {
        WebElement el = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void scrollIntoView(By locator) {
        WebElement el = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    public void hover(By locator) {
        WebElement el = waitForVisibility(locator);
        new Actions(driver).moveToElement(el).perform();
    }

    public void selectByVisibleText(By locator, String visibleText) {
        WebElement el = waitForVisibility(locator);
        new Select(el).selectByVisibleText(visibleText);
    }

    public void takeScreenshot(String fileName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = System.getProperty("user.dir") + "/screenshots/" + fileName;
            FileUtils.copyFile(src, new File(path));
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}




