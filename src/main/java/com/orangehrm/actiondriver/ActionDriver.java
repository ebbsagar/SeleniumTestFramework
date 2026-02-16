package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {

   private WebDriver driver;
   private WebDriverWait wait;

   public ActionDriver(WebDriver driver){
       this.driver=driver;
       int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
       this.wait = new WebDriverWait(driver , Duration.ofSeconds(explicitWait));
       System.out.println("Driver is created");
   }
      // Wait for elements to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            System.out.println("elements is not clickable: " +e.getMessage());
        }

    }

    // Wait for elements to be visible
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("elements is not visible: " +e.getMessage());
        }

    }

    // method to click an element
    public void click(By by){
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
        } catch (Exception e) {
            System.out.println("Unable to click element: " +e.getMessage());
        }

    }

    // method to enter input field
    public void enterText(By by, String value){
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
        } catch (Exception e) {
            System.out.println("Unable to send keys to input field: " +e.getMessage());
        }

    }

    //Method to get text prompt from an input field
    public String  getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            System.out.print("Unable to get text :" +e.getMessage());
            return "";
        }
    }

    //Method to compare Two text
    public boolean  compareText(By by, String expectedText){

        try {
            waitForElementToBeVisible(by);
            String actualText= driver.findElement(by).getText();
            if(expectedText.equals(actualText)) {
                System.out.print("Text are matching:" +actualText+ "equals" +expectedText);
                return true;
            }
            else {
                System.out.print("Text are not matching:" +actualText+ " not equals " +expectedText);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Unable to do string compare" +e.getMessage());
        }
        return false;
    }

    //Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            System.out.println("Element is not displayed" +e.getMessage());
            return false;
        }

    }
// wait for  page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            System.out.println("Page loaded successfully.");
        } catch (Exception e) {
            System.out.println("Page didn't load within " +timeOutInSec+ "seconds. Exception : "+e.getMessage());
        }

    }

    // Scroll to element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true);", element);
        } catch (Exception e) {
            System.out.println("Unable to locate element to scroll into view" +e.getMessage());
        }
    }
}
