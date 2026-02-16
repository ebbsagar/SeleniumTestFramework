package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private ActionDriver actionDriver;

    //Define locators by using class
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIDButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']/img");

    // Initialize the ActionDriver object
//    public HomePage(WebDriver driver){
//        this.actionDriver=new ActionDriver(driver);
//    }

    public HomePage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    //Method to verify if Admin tab is visible
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    //Verify Orange HRM logo visible
    public boolean verifyOrangeHRMlogo(){
        return actionDriver.isDisplayed(orangeHRMlogo);
    }

    //Method to perform logout operation
    public void logout(){
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }

}
