package Screens;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;


public class OnboardingScreen extends BaseScreen {
    AndroidDriver driver;

    private final By NEXT_BUTTON = By.id("right_arrow_touch");
    private final By GOT_IT_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Got it\")");

    public OnboardingScreen(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    private void tapNextButton() {
        waitAndClick(driver.findElement(NEXT_BUTTON));
    }

    public void completeOnboarding() {
       tapNextButton();
        if(isElementPresentWithinTime(NEXT_BUTTON,5)){
            while (!isElementFoundByLocator(GOT_IT_BUTTON)) {
                tapNextButton();
            }
            tapGotItButton();
        }
    }

    private void tapGotItButton() {
        waitAndClick(driver.findElement(GOT_IT_BUTTON));
    }
}
