package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class DummyTest extends BaseClass {

    @Test
    public void dummyTest(){
       String title = driver.getTitle();
       assert title.equals("OrangeHRM") : "Title is not matching..!!";

       System.out.println("Test Passed - Title is matching");
    }
}
