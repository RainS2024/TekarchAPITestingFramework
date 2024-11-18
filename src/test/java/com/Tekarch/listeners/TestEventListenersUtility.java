package com.Tekarch.listeners;

import com.Tekarch.utils.ExtentReportsUtility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestEventListenersUtility implements ITestListener {
	private static ExtentReportsUtility extentObject= ExtentReportsUtility.getInstance();

	@Override
	public void onTestStart(ITestResult result) { // before every @Test method called this method is executed
		extentObject.startSingleTestReport(result.getMethod().getMethodName());	
		System.out.println("test logger created="+extentObject.toString());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentObject.logTestpassed(result.getMethod().getMethodName()+" is passed");	
	}

	@Override
	public void onTestFailure(ITestResult result) {
		extentObject.logTestFailed(result.getMethod().getMethodName()+" is failed");
		extentObject.logTestFailedWithException(result.getThrowable());
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		//extentObject=ExtentReportsUtility.getInstance();
		extentObject.startExtentReport();
		System.out.println("report utility object created="+extentObject.toString());
	}

	@Override
	public void onFinish(ITestContext context) {
		extentObject.endReport();
		System.out.println("Report utility object closed "+extentObject.toString());
	}
	
}
