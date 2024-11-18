package com.Tekarch.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	public static final String CURR_DIR = System.getProperty("user.dir");
	public static final String TEST_RESOURCES = CURR_DIR + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	public static final String ENVIRONMENT_DETAILS_PROPERTIES_FILE = TEST_RESOURCES + File.separator + "EnvironmentDetails" + File.separator + "TechArk.properties";
	public static final String SPARKS_HTML_REPORT_PATH= CURR_DIR + File.separator + "Reports"+ File.separator + "CompleteReport" + new SimpleDateFormat("dd-MM-yyyy HH-mm").format(new Date()) +".html";
	public final static String TESTDATA_PROPERTIES_FILE = TEST_RESOURCES + File.separator + "Testdata" + File.separator + "Testdata.properties";

	
	
	
	
	
}

