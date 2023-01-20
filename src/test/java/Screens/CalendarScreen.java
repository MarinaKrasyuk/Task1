package Screens;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofSeconds;

public class CalendarScreen extends BaseScreen {
    AndroidDriver driver;

    private final By ADD_BUTTON = MobileBy.AccessibilityId("Create new event and more");
    private final By NEW_EVENT_BUTTON = MobileBy.xpath("//android.widget.TextView[@text='Event']");
    private final By DELETE_BUTTON = MobileBy.id("android:id/button1");

    private final String VALIDATION_XPATH_PATTERN = "//android.view.View[contains(@content-desc,'%s, %s – %s')]";
    private final String DELETE_XPATH_PATTERN = "//android.view.View[contains(@content-desc,'%s')]";


    public CalendarScreen(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    private void tapAddButton() {
        waitAndClick(ADD_BUTTON);
    }

    private void tapNewEventButton() {
        waitAndClick(NEW_EVENT_BUTTON);
    }

    public void navigateToNewEventScreen() {
        tapAddButton();
        tapNewEventButton();
    }

    public void validateEventIsCreated(String eventName, String startTime, String endTime) {
        Assert.assertTrue("Event is not created", isElementPresentWithinTime(By.xpath(String.format(VALIDATION_XPATH_PATTERN, eventName, startTime, endTime)), 10));
    }

    public void deleteEvent(String eventName) {
        By eventBy = MobileBy.xpath(String.format(DELETE_XPATH_PATTERN, eventName));
        if (isElementPresentWithinTime(eventBy, 5)) {
            MobileElement event = findElementByWithWait(eventBy);
            Dimension eventElementSize = event.getSize();
            int y = event.getCenter().getY();
            int startX = (int) (0.3 * eventElementSize.getWidth());
            int endX = (int) (0.9 * eventElementSize.getWidth());

            new TouchAction(driver)
                    .press(PointOption.point(startX, y))
                    .waitAction(waitOptions(ofSeconds(2)))
                    .moveTo(PointOption.point(endX, y))
                    .release()
                    .perform();
            driver.findElement(DELETE_BUTTON).click();
        }
    }
}
