package tests;

import driver.Driver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.net.MalformedURLException;

public class BaseTest {
    public static AndroidDriver driver;

    @BeforeAll
    public static void initialDriver() throws MalformedURLException {
        driver = Driver.getDriver();
    }

    @AfterAll
    public static void  tearDown() throws IOException {
        Driver.closeDriver(driver);
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
        Runtime.getRuntime().exec("adb kill-server");
    }
}
