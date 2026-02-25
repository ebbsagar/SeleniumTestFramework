package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;
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
   public static final Logger logger = BaseClass.logger;

   public ActionDriver(WebDriver driver) {
        this.driver = driver;
       int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
       this.wait = new WebDriverWait(driver , Duration.ofSeconds(explicitWait));
       logger.info("Driver is created");
   }



    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        // Check for null driver or locator to avoid NullPointerException
        if (driver == null) {
            return "Driver is not initialized.";
        }
        if (locator == null) {
            return "Locator is null.";
        }

        try {
            // Find the element using the locator
            WebElement element = driver.findElement(locator);

            // Get element attributes
            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");

            // Return a description based on available attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder: " + placeholder;
            } else {
                return "Element located using: " + locator.toString();
            }
        } catch (Exception e) {
            // Log exception for debugging
            e.printStackTrace(); // Replace with a logger in a real-world scenario
            return "Unable to describe element due to error: " + e.getMessage();
        }
    }

    // Utility method to check if a string is not null or empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }




      // Wait for elements to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
            getElementDescription(by);

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
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("Element clicked: " +elementDescription);
        } catch (Exception e) {
           logger.error("Unable to click element: " +e.getMessage());
        }

    }

    // method to enter input field
    public void enterText(By by, String value){
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
        } catch (Exception e) {
            logger.error("Unable to send keys to input field: {}", e.getMessage());
        }

    }

    //Method to get text prompt from an input field
    public String  getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get text :{}", e.getMessage());
            return "";
        }
    }

    //Method to compare Two text
    public boolean  compareText(By by, String expectedText){

        try {
            waitForElementToBeVisible(by);
            String actualText= driver.findElement(by).getText();
            if(expectedText.equals(actualText)) {
                logger.info("Text are matching:" +actualText+ "equals" +expectedText);
                return true;
            }
            else {
                logger.info("Text are not matching:" +actualText+ " not equals " +expectedText);
                return false;
            }
        } catch (Exception e) {
           logger.error("Unable to do string compare" +e.getMessage());
        }
        return false;
    }

    //Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            logger.info("Element is displayed " + getElementDescription(by));
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
           logger.error("Element is not displayed" +e.getMessage());
            return false;
        }

    }
// wait for  page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
           logger.error("Page didn't load within " +timeOutInSec+ "seconds. Exception : "+e.getMessage());
        }

    }

    // Scroll to element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true);", element);
        } catch (Exception e) {
            logger.error("Unable to locate element to scroll into view{}", e.getMessage());
        }
    }
}
