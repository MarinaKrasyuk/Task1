package utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import tests.CommonSteps;

public class Utils extends CommonSteps{

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG () {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }
}
