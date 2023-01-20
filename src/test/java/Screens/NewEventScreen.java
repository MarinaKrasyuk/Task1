package Screens;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class NewEventScreen extends BaseScreen{
    AndroidDriver driver;

    private final By EVENT_NAME_INPUT = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Enter title\")");
    private final By START_TIME_BUTTON = MobileBy.xpath("//android.widget.Button[contains(@content-desc,'Start time:')]");
    private final By TOGGLE_MODE = MobileBy.AccessibilityId("Switch to text input mode for the time input.");
    private final By HOURS_INPUT = MobileBy.id("android:id/input_hour");
    private final By MINUTES_INPUT = MobileBy.id("android:id/input_minute");
    private final By MARKER_FORMAT_INPUT = MobileBy.id("android:id/text1");
    private final By END_TIME_BUTTON = MobileBy.xpath("//android.widget.Button[contains(@content-desc,'End time:')]");
    private final By OK_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\")");
    private final By INPUT_TEXT_MODE = MobileBy.AccessibilityId("Switch to text input mode for the time input.");
    private final By SAVE_BUTTON = MobileBy.id("save");
    private final By ADD_LOCATION_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Add location\")");
    private final By ALLOW_PERMISSION_BUTTON = MobileBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private final By SEARCH_INPUT = MobileBy.id("search_field");
    private final By LOCATION_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Minsk\")");

    private final String XPATH_PATTERN = "//android.widget.CheckedTextView[contains(@text,'%s')]";

    public NewEventScreen(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    private void enterEventName(String eventName) {
        clearAndEnterTextToInput(EVENT_NAME_INPUT, eventName);
    }

    public void setUpEvent(String eventName,String locationName, String startHour, String startMinute, String startMarkerFormat,
                           String endHour, String endMinute, String endMarkerFormat) {
        enterEventName(eventName);
        waitAndClick(START_TIME_BUTTON);
        waitAndClick(TOGGLE_MODE);

        setUpTime(startHour,startMinute, startMarkerFormat);
        waitAndClick(END_TIME_BUTTON);
        waitAndClick(INPUT_TEXT_MODE);

        setUpTime(endHour,endMinute,endMarkerFormat);

        addLocation(locationName);

        waitAndClick(driver.findElement(SAVE_BUTTON));
    }

    private void setUpTime (String hour, String minute, String markerFormat) {
        enterTextToInput(driver.findElement(HOURS_INPUT),String.valueOf(hour));
        enterTextToInput(driver.findElement(MINUTES_INPUT),String.valueOf(minute));
        MobileElement markerFormatElement = (MobileElement) driver.findElement(MARKER_FORMAT_INPUT);
        if (!markerFormatElement.getText().equals(markerFormat)){
            waitAndClick(markerFormatElement);
            waitAndClick(driver.findElement(By.xpath(String.format(XPATH_PATTERN,markerFormat))));
        }
       waitAndClick(driver.findElement(OK_BUTTON));
    }

    public void addLocation(String locationName){
        waitAndClick(driver.findElement(ADD_LOCATION_BUTTON));
        waitAndClick(ALLOW_PERMISSION_BUTTON);
        clearAndEnterTextToInput(SEARCH_INPUT, locationName);
        waitAndClick(driver.findElement(LOCATION_BUTTON));
    }
}
