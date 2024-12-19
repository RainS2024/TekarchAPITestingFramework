package com.Tekarch.base;

import com.Tekarch.utils.EnvironmentDetails;
import com.Tekarch.utils.ExtentReportsUtility;
import com.Tekarch.utils.TestDataUtils;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance(); 
	
	
    @BeforeSuite
    public void beforeSuite() {
  
        EnvironmentDetails.loadProperties();
        TestDataUtils.loadProperties();
        report.startExtentReport();
        
        
    }
}
