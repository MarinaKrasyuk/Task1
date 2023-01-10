import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

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

    @Before
    public void setUp() throws MalformedURLException {
        URL driverUrl = new URL("http://0.0.0.0:4723/wd/hub");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName" , "UiAutomator2");
        caps.setCapability("udid", "emulator-5554");
        caps.setCapability("appActivity", "com.android.calendar.event.LaunchInfoActivity");
        caps.setCapability("appPackage", "com.google.android.calendar");

        driver = new AndroidDriver(driverUrl, caps);

        eventName = "Test Event";
    }

    @Test
    public void task1Test() throws InterruptedException {
        completeOnboarding();

        setUpEvent();

        validateEventIsCreated();
    }

    @After
    public void cleanUp() throws InterruptedException {
        deleteEvent();
        driver.quit();
    }

    private void completeOnboarding() throws InterruptedException {
        WebElement nextButton = driver.findElement(By.id("right_arrow_touch"));
        if(nextButton.isDisplayed()){
            while (driver.findElements(By.id("done_button")).size()==0) {
                nextButton.click();
                Thread.sleep(200);
            }
            driver.findElement(By.id("done_button")).click();
        }
    }

    private void setUpEvent() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.id("floating_action_button")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@content-desc='Event button']")).click();
        driver.findElement(By.id("title_edit_text")).sendKeys(eventName);
        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc,'Start time:')]")).click();
        WebElement toggleMode =  driver.findElement(By.id("android:id/toggle_mode"));
        toggleMode.click();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("KK:mm a", Locale.ENGLISH);

        LocalDateTime startDateTime = timestamp.toLocalDateTime().plusSeconds(60*60);
        startTime = formatter.format(startDateTime);

        setUpTime(startTime.substring(0,2),startTime.substring(3,5), startTime.substring(6,8));

        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc,'End time:')]")).click();
        Thread.sleep(2000);
        toggleMode.click();
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
        okButton = driver.findElement(By.id("android:id/button1"));
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
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='More options']")).click();
            driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Delete')]")).click();
            okButton.click();
        }

    }
}
