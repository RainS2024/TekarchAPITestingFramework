package com.Tekarch.CRUD;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import com.Tekarch.utils.ExtentReportsUtility;

import org.apache.http.HttpStatus;
import io.restassured.response.Response;
import com.Tekarch.utils.EnvironmentDetails;
import com.Tekarch.utils.JsonSchemaValidate;
import com.Tekarch.utils.TestDataUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.Tekarch.listeners.TestEventListenersUtility;
import com.Tekarch.listeners.ReportGenerator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.Tekarch.RequestPOJO.LoginRequestPOJO;
import com.Tekarch.ResponsePOJO.EditDataResponsePOJO;
import com.Tekarch.ResponsePOJO.LoginResponsePOJO;
import com.Tekarch.base.APIHelper;
import com.Tekarch.base.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

@Listeners(com.Tekarch.listeners.TestEventListenersUtility.class)
public class ValidateLoginFunctionality extends BaseTest {

ExtentReportsUtility report=ExtentReportsUtility.getInstance(); 
APIHelper apiHelper;
	
@BeforeClass
public void beforeClass() {
    apiHelper = new APIHelper();
    
}

@Test(priority = 0, description = "validate login functionality with valid credentials")
public void validateLoginWithValidCredentials() {
    Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
    
    Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_CREATED,"error occured with login");
   report.logTestInfo("successfull login with statuscode 201");
    JsonSchemaValidate.validateSchemaInClassPath(login,"ExpectedJsonSchema/LoginResponse.json");
   report.logTestInfo("LoginResponse is validated against expected schema successfully");
     
}

@Test(priority = 1, description = "validate login functionality with invalid credentials")
public void validateLoginWithInValidCredentials() {
    Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), "password");
    report.logTestInfo("unsuccessfull login attempted");
    Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "Login is not returning proper status code with invalid credentials.");
    report.logTestInfo(login.getStatusLine());
    System.out.println(login.getStatusLine());
    EditDataResponsePOJO statusResponse = login.as(EditDataResponsePOJO.class);
    Assert.assertEquals(statusResponse.getStatus(), TestDataUtils.getProperty("invalidCredentialsMessage"), "Status message is not returning as expected");
}



}
	
	