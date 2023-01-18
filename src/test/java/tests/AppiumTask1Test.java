package tests;

import Screens.CalendarScreen;
import Screens.NewEventScreen;
import Screens.OnboardingScreen;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.AllureId;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppiumTask1Test extends CommonSteps {
    private AndroidDriver driver;
    private String eventName;
    private String locationName;
    private String startTime;
    private String endTime;
    private OnboardingScreen onboardingScreen;
    private CalendarScreen calendarScreen;
    private NewEventScreen newEventScreen;

  @Before
    public void setUp() throws MalformedURLException {
        driver = initialDriver();
        eventName = "Test Event";
        locationName = "Minsk,Belarus";
        onboardingScreen = new OnboardingScreen(driver);
        calendarScreen = new CalendarScreen(driver);
        newEventScreen = new NewEventScreen(driver);
    }

    @Test
    @Tag("Calendar test")
    @AllureId("101")
    @Description("Set up Calendar event for Current time plus 1 hour")
    public void task1Test() {
        onboardingScreen.completeOnboarding();
        calendarScreen.navigateToNewEventScreen();
        setUpEventToTheCalendar();
    }

    @AfterAll
    public void cleanUp() throws IOException {
        calendarScreen.deleteEvent(eventName);
        driver.quit();
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }

    private void setUpEventToTheCalendar() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        LocalDateTime startDateTime = timestamp.toLocalDateTime().plusHours(1);
        startTime = formatter.format(startDateTime);

        LocalDateTime endDateTime = startDateTime.plusMinutes(90);
        endTime = formatter.format(endDateTime);

        newEventScreen.setUpEvent(eventName,locationName, startTime.substring(0,2),startTime.substring(3,5), startTime.substring(6,8),
                endTime.substring(0,2),endTime.substring(3,5), endTime.substring(6,8));
//        calendarScreen.validateEventIsCreated(eventName,startTime, endTime);
    }
}
