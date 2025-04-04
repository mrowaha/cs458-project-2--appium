import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class AppiumTest {

    public AndroidDriver driver;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        String appiumServerUrl = "http://127.0.0.1:4723";

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("platformName", "Android");
        dc.setCapability("appium:automationName", "uiautomator2");
        dc.setCapability("appium:app", System.getProperty("user.dir")+"/app/sut.apk");

        driver = new AndroidDriver(new URL(appiumServerUrl), dc);
    }

    @Test
    public void testNameRequiredValidation() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement error = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--fullname\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(error));
        Assert.assertEquals(error.getText(), "Name is required", "Expected required error message.");
    }

    @Test
    public void testNamePatternValidation() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Find and input an invalid name
        WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--fullname")
        ));
        inputField.sendKeys("123!@#");

        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement error = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--fullname\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(error));
        Assert.assertEquals(error.getText(), "Name contains invalid characters", "Expected required error message.");
    }

    @Test
    public void testInvalidBirthDate() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Find and input an invalid name
        WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--birthdate")
        ));
        inputField.sendKeys("123!@#");

        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement error = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--birthdate\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(error));
        Assert.assertEquals(error.getText(), "Not valid", "Expected required error message.");
    }

    @Test
    public void testInvalidCalendarBirthDate() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Find and input an invalid name
        WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--birthdate")
        ));
        inputField.sendKeys("31-02-2002"); // february never has 31 days

        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement error = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--birthdate\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(error));
        Assert.assertEquals(error.getText(), "Not valid", "Expected required error message.");
    }


    @AfterMethod
    public void close() {
        driver.quit();
    }

}
