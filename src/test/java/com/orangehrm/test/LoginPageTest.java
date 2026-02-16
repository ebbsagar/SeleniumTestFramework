package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage(){
       loginPage = new LoginPage(getDriver());
       homePage = new HomePage(getDriver());
    }
@Test
    public void verifyValidLoginTest(){
        loginPage.login(prop.getProperty("username"), prop.getProperty("password") );
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin Tab should be visible after successful Login");
        homePage.logout();
        staticWait(2);
    }
@Test
    public void invalidLoginTest(){
        loginPage.login("login", "password" );
        String expectedErrorMessage = "Invalid credentials1";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
    }
}
