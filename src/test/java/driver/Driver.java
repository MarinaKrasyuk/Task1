package driver;

import constant.ConstantEnv;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class Driver {

    private static final String UNLOCK_TYPE = "unlockType";
    private static final String UNLOCK_KEY = "unlockKey";

    public static AndroidDriver getDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, "Android");
        caps.setCapability(AUTOMATION_NAME , "UiAutomator2");
        caps.setCapability(UDID, "emulator-5554");
        caps.setCapability(NO_RESET, "false");
        caps.setCapability(APP_ACTIVITY, "com.android.calendar.event.LaunchInfoActivity");
        caps.setCapability(APP_PACKAGE, "com.google.android.calendar");
        caps.setCapability(UNLOCK_TYPE, "pin");
        caps.setCapability(UNLOCK_KEY, "1111");
        caps.setCapability(AVD, "Pixel_6_Pro_API_29");
        caps.setCapability(AVD_LAUNCH_TIMEOUT, "1000000");
        caps.setCapability(AVD_READY_TIMEOUT, "1000000");

        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        return new AndroidDriver(new URL(ConstantEnv.URL_NAME), caps);
    }

    public static void closeDriver(AndroidDriver driver){
        System.out.print("close(): ");
        if (driver != null) {
            driver.quit();
        }
    }
}
