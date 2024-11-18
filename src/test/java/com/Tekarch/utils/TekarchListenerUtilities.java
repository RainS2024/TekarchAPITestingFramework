package com.Tekarch.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;



public class TekarchListenerUtilities implements ITestListener {
	
	private static ExtentReportsUtility extentreport = ExtentReportsUtility.getInstance();
	@Test
	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extentreport.endReport();
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		extentreport.startExtentReport();
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
	
		extentreport.logTestFailed(result.getMethod().getMethodName()+"....... execution completed with failure..............");
		String filename=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(0));
		System.out.println("filename="+filename);
		extentreport.logTestFailedWithException(result.getThrowable());
	}

	@Override
	public void onTestStart(ITestResult result) {
		extentreport.startSingleTestReport(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		extentreport.logTestpassed(result.getMethod().getMethodName()+ "---Execution completed with success---");
	}

}
