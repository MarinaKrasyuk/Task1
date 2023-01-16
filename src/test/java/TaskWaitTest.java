import Screens.CalendarScreen;
import Screens.NewEventScreen;
import Screens.NotificationScreen;
import Screens.OnboardingScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TaskWaitTest extends CommonSteps{
    private AndroidDriver driver;
    private String startTime;
    private LocalDateTime startDateTime;
    private String eventName;
    private String locationName;
    private OnboardingScreen onboardingScreen;
    private CalendarScreen calendarScreen;
    private NewEventScreen newEventScreen;
    private NotificationScreen notificationScreen;


    @Before
    public void setUp() throws MalformedURLException {
        driver = initialDriver();
        eventName = "Event for Current time";
        locationName = "Minsk,Belarus";
        onboardingScreen = new OnboardingScreen(driver);
        calendarScreen = new CalendarScreen(driver);
        newEventScreen = new NewEventScreen(driver);
        notificationScreen = new NotificationScreen(driver);
    }

    @Test
    public void taskWaitTest() {
        onboardingScreen.completeOnboarding();
        calendarScreen.navigateToNewEventScreen();
        setUpEventToTheCalendar();
        validationPushNotificationIsReceived();
    }

    private void validationPushNotificationIsReceived() {
        while(new Timestamp(System.currentTimeMillis()).toLocalDateTime().isBefore(startDateTime)){}

        driver.openNotifications();

        Assert.assertTrue("Text of Event is not correct!", notificationScreen.getTextOfEvent().contains(eventName));
        Assert.assertTrue( "Time of Event is not correct!", notificationScreen.getTimeOfEvent().contains(startTime.substring(0,5)));

        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }

    private void setUpEventToTheCalendar() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

         startDateTime = timestamp.toLocalDateTime().plusMinutes(1);
         startTime = formatter.format(startDateTime);

        LocalDateTime endDateTime = startDateTime.plusMinutes(10);
        String endTime = formatter.format(endDateTime);

        newEventScreen.setUpEvent(eventName,locationName, startTime.substring(0,2),startTime.substring(3,5), startTime.substring(6,8),
                endTime.substring(0,2),endTime.substring(3,5), endTime.substring(6,8));
        calendarScreen.validateEventIsCreated(eventName,startTime, endTime);
    }

    @After
    public void cleanUp() throws IOException {
        calendarScreen.deleteEvent(eventName);
        driver.quit();
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }
}