package tests;

import Screens.CalendarScreen;
import Screens.NewEventScreen;
import Screens.OnboardingScreen;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
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
public class AppiumTask1Test extends BaseTest {
    private String eventName;
    private String locationName;
    private String startTime;
    private String endTime;
    private OnboardingScreen onboardingScreen;
    private CalendarScreen calendarScreen;
    private NewEventScreen newEventScreen;

  @BeforeEach
    public void setUp(){
        eventName = "Test Event";
        locationName = "Minsk,Belarus";
        onboardingScreen = new OnboardingScreen(driver);
        calendarScreen = new CalendarScreen(driver);
        newEventScreen = new NewEventScreen(driver);
    }

    @AllureId("101")
    @Description("Set up Calendar event for Current time plus 1 hour")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void task1Test() {
        onboardingScreen.completeOnboarding();
        calendarScreen.navigateToNewEventScreen();
        setUpEventToTheCalendar();
    }

    @AfterEach
    public void cleanUp() {
        calendarScreen.deleteEvent(eventName);
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
        calendarScreen.validateEventIsCreated(eventName,startTime);
    }
}
