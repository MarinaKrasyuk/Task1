package Screens;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class NotificationScreen extends BaseScreen {
    AndroidDriver driver;

    private final By NOTIFICATION_XPATH = MobileBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.ScrollView/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout");

    public NotificationScreen(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    private WebElement findNotificationByAppName() {
       return driver.findElement(NOTIFICATION_XPATH);
    }

    public String getTextOfEvent(){
        WebElement element = findNotificationByAppName().findElement(By.xpath("//*[@resource-id='android:id/title']"));
        return element.getText();
    }

    public String getTimeOfEvent(){
        WebElement element = findNotificationByAppName().findElement(By.xpath("//*[@resource-id='android:id/big_text']"));
        return element.getText();
    }

}
