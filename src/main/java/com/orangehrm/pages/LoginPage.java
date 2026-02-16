package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private ActionDriver actionDriver;
    private By usernameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[text()=' Login ']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

//    public LoginPage(WebDriver driver) {
//        this.actionDriver = new ActionDriver(driver);
//    }

    public LoginPage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Method to perform login
    public void login(String userName, String password) {
        actionDriver.enterText(usernameField, userName);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    //Method to check error message on login
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to get the text from error message
    public String getErrorMessageText() {
        return actionDriver.getText(errorMessage);
    }

    //Verify if error is correct or not
    public boolean verifyErrorMessage(String expectedError) {
       return actionDriver.compareText(errorMessage, expectedError);
    }
}
