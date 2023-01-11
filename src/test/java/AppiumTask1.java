import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AppiumTask1 {
    private AndroidDriver driver;
    private String eventName ;
    private String startTime;
    private String endTime;
    private WebElement okButton;

    private static final String PLATFORM_NAME = "platformName";
    private static final String AUTOMATION_NAME = "automationName";
    private static final String UDID = "udid";
    private static final String APP_ACTIVITY = "appActivity";
    private static final String APP_PACKAGE = "appPackage";
    private static final String NO_RESET = "noReset";
    private static final String UNLOCK_TYPE = "unlockType";
    private static final String UNLOCK_KEY = "unlockKey";
    private static final String AVD = "avd";
    private static final String AVD_LAUNCH_TIMEOUT = "avdLaunchTimeout";
    private static final String AVD_READY_TIMEOUT = "avdReadyTimeout";

    @Before
    public void setUp() throws MalformedURLException {
        URL driverUrl = new URL("http://0.0.0.0:4723/wd/hub");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, "Android");
        caps.setCapability(AUTOMATION_NAME , "UiAutomator2");
        caps.setCapability(UDID, "emulator-5554");
        caps.setCapability(NO_RESET, "false");
        caps.setCapability(APP_ACTIVITY, "com.android.calendar.event.LaunchInfoActivity");
        caps.setCapability(APP_PACKAGE, "com.google.android.calendar");
        caps.setCapability(UNLOCK_TYPE, "pin");
        caps.setCapability(UNLOCK_KEY, "1111");
        caps.setCapability(AVD, "Pixel_XL_API_29");
        caps.setCapability(AVD_LAUNCH_TIMEOUT, "300000");
        caps.setCapability(AVD_READY_TIMEOUT, "300000");

        driver = new AndroidDriver(driverUrl, caps);

        eventName = "Test Event";
    }

    @Test
    public void task1Test() throws InterruptedException {
        completeOnboarding();

        setUpEvent();

//        validateEventIsCreated();
    }

    @After
    public void cleanUp() throws InterruptedException, IOException {
        deleteEvent();
        driver.quit();
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }

    private void completeOnboarding() throws InterruptedException {
        WebElement nextButton = driver.findElement(By.id("right_arrow_touch"));
        if(nextButton.isDisplayed()){
            while (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Got it\")")).size()==0) {
                nextButton.click();
                Thread.sleep(200);
            }
            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Got it\")")).click();
        }
    }

    private void setUpEvent() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(MobileBy.AccessibilityId("Create new event and more")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='Event']")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Enter title\")")).sendKeys(eventName);
        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc,'Start time:')]")).click();
         WebElement toggleMode =  driver.findElement(MobileBy.AccessibilityId("Switch to text input mode for the time input."));
        toggleMode.click();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("KK:mm a", Locale.ENGLISH);

        LocalDateTime startDateTime = timestamp.toLocalDateTime().plusSeconds(60*60);
        startTime = formatter.format(startDateTime);

        setUpTime(startTime.substring(0,2),startTime.substring(3,5), startTime.substring(6,8));

        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc,'End time:')]")).click();
        Thread.sleep(2000);
        driver.findElement(MobileBy.AccessibilityId("Switch to text input mode for the time input.")).click();
        LocalDateTime endDateTime = startDateTime.plusSeconds(90 * 60);
        endTime = formatter.format(endDateTime);
        setUpTime(endTime.substring(0,2),endTime.substring(3,5), endTime.substring(6,8));

        driver.findElement(By.id("save")).click();

    }

    private void setUpTime (String hour, String minute, String markerFormat) throws InterruptedException {
        driver.findElement(By.id("android:id/input_hour")).sendKeys(String.valueOf(hour));
        driver.findElement(By.id("android:id/input_minute")).sendKeys(String.valueOf(minute));
        WebElement markerFormatInput = driver.findElement(By.id("android:id/text1"));
        if (!markerFormatInput.getText().equals(markerFormat)){
            markerFormatInput.click();
            Thread.sleep(1000);
            driver.findElement(By.xpath(String.format("//android.widget.CheckedTextView[contains(@text,'%s')]",markerFormat))).click();
        }
        okButton = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\")"));
        okButton.click();
    }

    private void validateEventIsCreated(){
        WebElement event = driver.findElement(By.xpath(String.format("//android.view.View[contains(@content-desc,'%s, %s – %s')]", eventName, startTime,endTime)));
        Assert.assertTrue("Event is not created", event.isDisplayed());
    }

    private void deleteEvent() throws InterruptedException {
        Thread.sleep(2000);
        WebElement event = driver.findElement(By.xpath(String.format("//android.view.View[contains(@content-desc,'%s')]", eventName)));
        if (event.isDisplayed()) {
            event.click();
            Thread.sleep(2000);
            driver.findElement(MobileBy.AccessibilityId("More options")).click();
            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Delete\")")).click();
            driver.findElement(MobileBy.id("android:id/button1")).click();
        }

    }
}
