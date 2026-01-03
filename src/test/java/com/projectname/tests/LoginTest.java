package com.projectname.tests;

import org.testng.annotations.*;
import Utilities.ExcelUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import testBase.LoginPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    LoginPage login;

    String excelPath = "C:\\SeleniumWorkSpace\\admin-app-automation\\testdata\\Book 32.xlsx";
    String sheetName = "Sheet1";
    Object[][] loginData;

    @BeforeClass
    public void loadData() {
        Object[][] rawData = ExcelUtils.getSheetData(excelPath, sheetName);

        loginData = new Object[rawData.length][3];
        for (int i = 0; i < rawData.length; i++) {
            loginData[i][0] = rawData[i][0]; // email
            loginData[i][1] = rawData[i][1]; // password
            loginData[i][2] = rawData[i][2]; // expectedResult
        }
    }

    @BeforeMethod
    public void setup() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://ldep.smartklinic.com:8443/saveplusdemo/Users/Admin_Login");
        Thread.sleep(5000); // wait after URL load

        login = new LoginPage(driver);
    }

    @DataProvider(name = "LoginData")
    public Object[][] getData() {
        return loginData;
    }

    @Test(dataProvider = "LoginData")
    public void verifyLogin(String email, String password, String expectedResult) {

        int rowNum = -1;

        try {
            rowNum = getRowNum(email, password);

            if (rowNum == -1) {
                System.out.println("⚠ Row not found → " + email);
                return;
            }

            // Perform login with waits inside LoginPage
            login.loginToAdmin(email, password);

            // Extra wait after clicking login
            Thread.sleep(5000);

            // Wait for home page URL
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            boolean loginSuccess = false;

            try {
                wait.until(ExpectedConditions.urlContains("/Administrator/Home"));
                loginSuccess = true;
            } catch (Exception ex) {
                loginSuccess = false;
            }

            String actualResult = loginSuccess ? "PASS" : "FAIL";

            ExcelUtils.writeResult(excelPath, sheetName, rowNum, 3, actualResult);

            System.out.println("✔ Row " + rowNum + " → " + actualResult);

        } catch (Exception e) {
            System.out.println("❌ Test failed for → " + email);
            e.printStackTrace();

            if (rowNum != -1) {
                try {
                    ExcelUtils.writeResult(excelPath, sheetName, rowNum, 3, "FAIL");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Find row number
    private int getRowNum(String email, String password) {
        for (int i = 0; i < loginData.length; i++) {
            if (loginData[i][0].toString().equals(email) &&
                loginData[i][1].toString().equals(password)) {
                return i + 1;
            }
        }
        return -1;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
