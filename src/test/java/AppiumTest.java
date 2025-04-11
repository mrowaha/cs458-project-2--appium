import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
        dc.setCapability("appium:app", System.getProperty("user.dir")+"/app/sut-v3.apk");

        driver = new AndroidDriver(new URL(appiumServerUrl), dc);
    }

    @Test
    public void testValidationFeedback() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement nameInputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--fullname")
        ));
        nameInputField.sendKeys("123!@#");

        WebElement bdInputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--birthdate")
        ));
        bdInputField.sendKeys("123!@#");


        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement nameError = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--fullname\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(nameError));
        Assert.assertEquals(nameError.getText(), "Name contains invalid characters", "Expected required error message.");
        WebElement bdError = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--birthdate\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(bdError));
        Assert.assertEquals(bdError.getText(), "Birth Date is invalid", "Expected required error message.");
        nameInputField.clear();
        nameInputField.sendKeys("cs project");
        bdInputField.clear();
        bdInputField.sendKeys("31-02-2002");
        Assert.assertEquals(bdError.getText(), "Birth Date is invalid");

        Assert.assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(
                        AppiumBy.accessibilityId("survey-error--fullname")
                )
        );
    }

    @Test
    public  void testRequiredFields() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();
        WebElement nameError = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--fullname\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(nameError));
        Assert.assertEquals(nameError.getText(), "Name is required", "Expected required error message.");
        WebElement bdError = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--birthdate\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(bdError));
        Assert.assertEquals(bdError.getText(), "Birth Date is required", "Expected required error message.");

    }

    @Test
    public  void testDynamicDefectFields() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        // Scroll to the element with content-desc "ChatGPT"
        WebElement chatGPTcheckBoxContainer = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"ChatGPT\"))"
        ));

        WebElement copilotcheckBoxContainer = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"Copilot\"))"
        ));

        // Now locate the inner ViewGroup from the checkbox container
        WebElement gptcheckBoxViewGroup = chatGPTcheckBoxContainer.findElement(By.className("android.view.ViewGroup"));
        gptcheckBoxViewGroup.click();
        WebElement pilotcheckBoxViewGroup = copilotcheckBoxContainer.findElement(By.className("android.view.ViewGroup"));
        pilotcheckBoxViewGroup.click();
        new WebDriverWait(driver, Duration.ofSeconds(1));

        WebElement gptDefectField = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"survey-field--modelcons-chatgpt\"))"
        ));

        gptDefectField.sendKeys("defect");
        WebElement pilotDefectField = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"survey-field--modelcons-copilot\"))"
        ));
        pilotDefectField.sendKeys("defect");
        pilotcheckBoxViewGroup.click();
        pilotcheckBoxViewGroup.click();
        Assert.assertEquals(pilotDefectField.getText().length(), 0);
    }

    @Test
    public void testMaximumInput() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        // Scroll to the element with content-desc "ChatGPT"
        WebElement chatGPTcheckBoxContainer = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"ChatGPT\"))"
        ));

        WebElement copilotcheckBoxContainer = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"Copilot\"))"
        ));

        // Now locate the inner ViewGroup from the checkbox container
        WebElement gptcheckBoxViewGroup = chatGPTcheckBoxContainer.findElement(By.className("android.view.ViewGroup"));
        gptcheckBoxViewGroup.click();
        WebElement pilotcheckBoxViewGroup = copilotcheckBoxContainer.findElement(By.className("android.view.ViewGroup"));
        pilotcheckBoxViewGroup.click();
        new WebDriverWait(driver, Duration.ofSeconds(1));

        WebElement gptDefectField = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"survey-field--modelcons-chatgpt\"))"
        ));

        gptDefectField.sendKeys("this will exceed the world limit");
        WebElement pilotDefectField = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"survey-field--modelcons-copilot\"))"
        ));
        pilotDefectField.sendKeys("defect");

        WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Submit\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        submitButton.click();

        WebElement gptMaxLengthError = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"survey-error--modelcons-chatgpt\"))"
        ));
        wait.until(ExpectedConditions.visibilityOf(gptMaxLengthError));
        Assert.assertEquals(gptMaxLengthError.getText(), "Defect/con cannot exceed 10 characters", "Expected required error message.");
        Assert.assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(
                        AppiumBy.accessibilityId("survey-error--modelcons-copilot")
                )
        );
    }

    @Test
    public  void testFormReset() {
        driver.findElement(AppiumBy.accessibilityId("testButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement nameInputField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("survey-field--fullname")
        ));
        nameInputField.sendKeys("cs project");

        // Scroll to the element with content-desc "ChatGPT"
        WebElement chatGPTcheckBoxContainer = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"ChatGPT\"))"
        ));
        // Now locate the inner ViewGroup from the checkbox container
        WebElement gptcheckBoxViewGroup = chatGPTcheckBoxContainer.findElement(By.className("android.view.ViewGroup"));
        gptcheckBoxViewGroup.click();
        new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement gptDefectField = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"survey-field--modelcons-chatgpt\"))"
        ));
        gptDefectField.sendKeys("defect");

        WebElement resetButton = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().description(\"Reset\"))"
        ));
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
        resetButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(1));
        Assert.assertEquals(nameInputField.getText().length(), 0);
        Assert.assertThrows(
                NoSuchElementException.class,
                () -> driver.findElement(
                        AppiumBy.accessibilityId("survey-field--modelcons-chatgpt")
                )
        );
    }



    @AfterMethod
    public void close() {
        driver.quit();
    }

}
