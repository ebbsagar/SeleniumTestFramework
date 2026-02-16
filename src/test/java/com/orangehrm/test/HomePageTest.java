package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyOrangeHRMLogo(){
        loginPage.login(prop.getProperty("username"), prop.getProperty("password") );
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin Tab should be visible after successful Login");
        Assert.assertTrue(homePage.verifyOrangeHRMlogo(),"Logo is not visible");
        homePage.logout();
    }

}
