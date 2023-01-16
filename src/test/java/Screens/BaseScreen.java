package Screens;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BaseScreen {
    AndroidDriver driver;
    WebDriverWait wait;

    BaseScreen(AndroidDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    protected boolean isElementPresentWithinTime(By by, long time) {
        try {
            new WebDriverWait(driver, time).until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private ExpectedCondition<MobileElement> elementIdDisplayed(By by) {
        return driver -> {
            List<MobileElement> listMobileElemets;
            listMobileElemets = driver.findElements(by);
            if (listMobileElemets.size() > 0 && listMobileElemets.get(0).isDisplayed()) {
                return listMobileElemets.get(0);
            }else return null;
        };
    }

    protected MobileElement findElementByWithWait(By by) {
       return wait.until(elementIdDisplayed(by));
    }

    public void waitAndClick(By by){
        findElementByWithWait(by).click();
    }

    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    protected boolean isElementFoundByLocator(By elementLocator) {
        return driver.findElements(elementLocator).size() > 0;
    }

    protected void clearAndEnterTextToInput(By by, String text) {
        findElementByWithWait(by).clear();
        findElementByWithWait(by).sendKeys(text);
    }

    protected void enterTextToInput(WebElement input, String text) {
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.sendKeys(text);
    }

}
