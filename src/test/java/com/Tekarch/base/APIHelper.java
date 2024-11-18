package com.Tekarch.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import com.Tekarch.RequestPOJO.CreateDataPOJO;
import com.Tekarch.RequestPOJO.DeleteDataPOJO;
import com.Tekarch.RequestPOJO.LoginRequestPOJO;
import com.Tekarch.RequestPOJO.UpdateDataRequestPOJO;
import com.Tekarch.ResponsePOJO.LoginResponsePOJO;
import com.Tekarch.utils.EnvironmentDetails;
import com.Tekarch.utils.ExtentReportsUtility;

import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class APIHelper {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance(); 
    RequestSpecification reqSpec;
    String token = "";

    public APIHelper() {
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given();
       
    }

    public Response login(String username, String password) {
        LoginRequestPOJO loginRequest = LoginRequestPOJO.builder().username(username).password(password).build(); // payload 
    
        reqSpec.headers(getHeaders(true));
        Response response = null;
        try {
            reqSpec.body(loginRequest); //Serializing loginrequest class to byte stream
             response = reqSpec.post("/login");
            if (response.getStatusCode() == HttpStatus.SC_CREATED) {
                List<LoginResponsePOJO> loginResponse = response.getBody().as(new TypeRef<List<LoginResponsePOJO>>() {
                });
                this.token = loginResponse.get(0).getToken();
                System.out.println("token====="+token);
                report.logTestInfo("token====="+token);
            }
        } catch (Exception e) {
            Assert.fail("Login functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response getData() {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            response = reqSpec.get("/getdata");
           // response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
            report.logTestFailed("Get data is failing due to :: "+ e.getMessage());
        }
        return response;
    }

    public Response CreateData(CreateDataPOJO CreateDataRequest) {
        reqSpec = RestAssured.given();
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(CreateDataRequest));
            reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(CreateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.post("/addData");
            //response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Add data functionality is failing due to :: " + e.getMessage());
            report.logTestFailed("Add data is failing due to :: "+ e.getMessage());
        }
        return response;
    }

    public Response putData(UpdateDataRequestPOJO updateDataRequest) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(updateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.put("/updateData");
           // response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update data functionality is failing due to :: " + e.getMessage());
            report.logTestFailed("Update data is failing due to :: "+ e.getMessage());
        }
        return response;
    }

    public Response deleteData(DeleteDataPOJO deleteDataRequest) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.delete("/deleteData");
            //response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
            report.logTestFailed("Delete data is failing due to :: "+ e.getMessage());
        }
        return response;
    }

    public HashMap<String, String> getHeaders(boolean forLogin) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        if (!forLogin) {
            headers.put("token", token);
        }
        return headers;
    }

    
    
    
}
