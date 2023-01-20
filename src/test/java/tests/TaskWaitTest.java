package tests;

import Screens.CalendarScreen;
import Screens.NewEventScreen;
import Screens.NotificationScreen;
import Screens.OnboardingScreen;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RunnerExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ExtendWith(RunnerExtension.class)
public class TaskWaitTest extends BaseTest {
    private String startTime;
    private LocalDateTime startDateTime;
    private String eventName;
    private String locationName;
    private OnboardingScreen onboardingScreen;
    private CalendarScreen calendarScreen;
    private NewEventScreen newEventScreen;
    private NotificationScreen notificationScreen;


    @BeforeEach
    public void setUp()  {
        eventName = "Event for Current time";
        locationName = "Minsk,Belarus";
        onboardingScreen = new OnboardingScreen(driver);
        calendarScreen = new CalendarScreen(driver);
        newEventScreen = new NewEventScreen(driver);
        notificationScreen = new NotificationScreen(driver);
    }

    @Test
    @AllureId("102")
    @Description("Set up Calendar event for Current time plus 1 minute and validation Notification")
    @Severity(SeverityLevel.NORMAL)
    public void taskWaitTest() {
        onboardingScreen.completeOnboarding();
        calendarScreen.navigateToNewEventScreen();
        setUpEventToTheCalendar();
        validationPushNotificationIsReceived();
    }

    private void validationPushNotificationIsReceived() {
        while (new Timestamp(System.currentTimeMillis()).toLocalDateTime().isBefore(startDateTime)) {
        }

        driver.openNotifications();

        Assert.assertTrue("Text of Event is not correct!", notificationScreen.getTextOfEvent().contains(eventName));
        Assert.assertTrue("Time of Event is not correct!", notificationScreen.getTimeOfEvent().contains(startTime.substring(1, 5)));

        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }

    private void setUpEventToTheCalendar() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        startDateTime = timestamp.toLocalDateTime().plusMinutes(1);
        startTime = formatter.format(startDateTime);

        LocalDateTime endDateTime = startDateTime.plusMinutes(10);
        String endTime = formatter.format(endDateTime);

        newEventScreen.setUpEvent(eventName, locationName, startTime.substring(0, 2), startTime.substring(3, 5), startTime.substring(6, 8),
                endTime.substring(0, 2), endTime.substring(3, 5), endTime.substring(6, 8));
        calendarScreen.validateEventIsCreated(eventName, startTime);
    }

    @AfterEach
    public void cleanUp() {
        calendarScreen.deleteEvent(eventName);
    }
}