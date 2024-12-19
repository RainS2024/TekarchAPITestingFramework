package com.Tekarch.CRUD;

import com.github.javafaker.Faker;

import com.Tekarch.base.APIHelper;
import com.Tekarch.base.BaseTest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import com.Tekarch.RequestPOJO.CreateDataPOJO;
import com.Tekarch.RequestPOJO.DeleteDataPOJO;
import com.Tekarch.RequestPOJO.UpdateDataRequestPOJO;
import com.Tekarch.ResponsePOJO.EditDataResponsePOJO;
import com.Tekarch.ResponsePOJO.GetDataResponse;
import com.Tekarch.ResponsePOJO.LoginResponsePOJO;
import com.Tekarch.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners(com.Tekarch.listeners.TestEventListenersUtility.class)
public class ValidateAddAndDeleteData_Functionality extends BaseTest {
	ExtentReportsUtility report=ExtentReportsUtility.getInstance(); 
	APIHelper apiHelper;
    String userid, accountno, departmentno, salary, pincode;
    private Faker faker;
    String dataId = "";

    @BeforeClass
    public void beforeClass() {
        faker = new Faker();
        apiHelper = new APIHelper();
    }
        
 @Test(priority = -1, description = "validate login functionality with valid credentials")
        public void validateLoginWithValidCredentials() {
            Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
            
            Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_CREATED,"error occured with login");
            report.logTestInfo("successfull login with statuscode 201");
            JsonSchemaValidate.validateSchemaInClassPath(login,"ExpectedJsonSchema/LoginResponse.json");
            report.logTestInfo("LoginResponse is validated against expected schema successfully");
            userid = login.getBody().as(new TypeRef<List<LoginResponsePOJO>>() {}).get(0).getUserid();
        }
       // Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
       //
        
    

    @Test(priority = 0, description = "validate add data functionality")
    public void validateAddDataFunctionality() {
        accountno = "Rain-" + faker.number().numberBetween(1000, 2000);
        departmentno = "5";
        salary = faker.number().numberBetween(5000, 8000) + "";
        pincode = faker.address().zipCode();
        CreateDataPOJO addDataRequest = CreateDataPOJO.builder().accountno(accountno).departmentno(departmentno).salary(salary).pincode(pincode).build();
        Response response = apiHelper.CreateData(addDataRequest);
        report.logTestInfo("Account got created -->" + accountno);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "Add data functionality is not working as expected.");
        report.logTestInfo("Status Code" + response.getStatusLine());
        Assert.assertEquals(response.as(EditDataResponsePOJO.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
        JsonSchemaValidate.validateSchema(response.asPrettyString(), "EditData.json");

    }

    @Test(priority = 1, description = "validate added data in the get data object", dependsOnMethods = "validateAddDataFunctionality")
    public void validateAddedDataInGetData() {
        Response data = apiHelper.getData();
        System.out.println("------------------Account Created------------------------");
        System.out.println("The Account no.created is " + data.getBody().jsonPath().get("[0].accountno"));
        System.out.println("The Department no.created is " + data.getBody().jsonPath().get("[0].departmentno"));
        System.out.println("The Salary created is " + data.getBody().jsonPath().get("[0].salary"));
        System.out.println("The pincode created is " + data.getBody().jsonPath().get("[0].pincode"));
        List<GetDataResponse> getDataResponseList = data.getBody().as(new TypeRef<List<GetDataResponse>>() {
        });
        report.logTestInfo("Account got created -->" + getDataResponseList.get(0));
        System.out.println("The Account created is " + getDataResponseList.get(0));
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is matching for get data.");
        report.logTestInfo("Status Code" + data.getStatusLine());
        GetDataResponse getDataResponse = null;
        try {
            getDataResponse = returnTheMatchingGetDataResponse(accountno, userid, getDataResponseList);
        } catch (NullPointerException e) {
            Assert.fail("Added data is not available in the get data response");
        }
        dataId = getDataResponse.getId();
        Assert.assertEquals(getDataResponse.getDepartmentno(), departmentno, "Add data functionality is not working as expected, Department number is not matching");
        Assert.assertEquals(getDataResponse.getSalary(), salary, "Add data functionality is not working as expected, Salary is not matching");
        Assert.assertEquals(getDataResponse.getPincode(), pincode, "Add data functionality is not working as expected, Pincode is not matching");
    }
    @Test(priority = 2, description = "validate update data functionality", dependsOnMethods = "validateAddDataFunctionality")
    public void validateUpdateDataFunctionality() {
    	accountno = "Rainbow-" + faker.number().numberBetween(1000, 2000);
        departmentno = "7";
        salary = faker.number().numberBetween(5000, 8000) + "";
        pincode = faker.address().zipCode();
    	UpdateDataRequestPOJO updateDataRequest = UpdateDataRequestPOJO.builder().accountno(accountno).departmentno(departmentno).salary(salary).pincode(pincode).userid(userid).id(dataId).build();
        Response response = apiHelper.putData(updateDataRequest);
       report.logTestInfo("Account got updated -->" + updateDataRequest.getAccountno());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "Add data functionality is not working as expected.");
       report.logTestInfo("Status Code" + response.getStatusLine());
        Assert.assertEquals(response.as(EditDataResponsePOJO.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
        JsonSchemaValidate.validateSchema(response.asPrettyString(), "EditData.json");

    }
    @Test(priority = 3, description = "validate updated data in the get data object", dependsOnMethods = "validateUpdateDataFunctionality")
    public void validateUpdateDataInGetData() {
        Response data = apiHelper.getData();
        System.out.println("------------------Account ater Update------------------------");
        System.out.println("The Account no.updated is " + data.getBody().jsonPath().get("[0].accountno"));
        System.out.println("The Department no.updated is " + data.getBody().jsonPath().get("[0].departmentno"));
        System.out.println("The Salary updated is " + data.getBody().jsonPath().get("[0].salary"));
        System.out.println("The pincode upfated is " + data.getBody().jsonPath().get("[0].pincode"));
        List<GetDataResponse> getDataResponseList = data.getBody().as(new TypeRef<List<GetDataResponse>>() {
        });
        report.logTestInfo("Account got updated -->" + getDataResponseList.get(0));
        System.out.println("The Account updated is " + getDataResponseList.get(0));
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is matching for get data.");
        report.logTestInfo("Status Code" + data.getStatusLine());
        GetDataResponse getDataResponse = null;
        try {
            getDataResponse = returnTheMatchingGetDataResponse(accountno, userid, getDataResponseList);
        } catch (NullPointerException e) {
            Assert.fail("Updated data is not available in the get data response");
        }
        dataId = getDataResponse.getId();
        Assert.assertEquals(getDataResponse.getDepartmentno(), departmentno, "Add data functionality is not working as expected, Department number is not matching");
        Assert.assertEquals(getDataResponse.getSalary(), salary, "Add data functionality is not working as expected, Salary is not matching");
        Assert.assertEquals(getDataResponse.getPincode(), pincode, "Add data functionality is not working as expected, Pincode is not matching");
    }
    
    @Test(priority = 4, description = "delete data functionality", dependsOnMethods = "validateUpdateDataFunctionality")
    public void validateDeleteData() {
    	DeleteDataPOJO deleteDataRequest = DeleteDataPOJO.builder().userid(userid).id(dataId).build();
        Response data = apiHelper.deleteData(deleteDataRequest);
        
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Delete data functionality is not working as expected.");
        report.logTestInfo("Status Code" + data.getStatusLine());
        Assert.assertEquals(data.as(EditDataResponsePOJO.class).getStatus(), TestDataUtils.getProperty("successStatusMessage"), "The value of status key is not as expected in response ");
        System.out.println("------------------Account deleted------------------------");
        String actualResponse = data.jsonPath().prettyPrint();
        JsonSchemaValidate.validateSchema(actualResponse, "EditData.json");
    }

    @Test(priority = 5, description = "validate deleted data in the get data object", dependsOnMethods = "validateDeleteData")
    public void validateDeletedDataInGetData() {
        Response data = apiHelper.getData();
        List<GetDataResponse> getDataResponseList = data.getBody().as(new TypeRef<List<GetDataResponse>>() {
        });
        report.logTestInfo("Account got deleted and first account in the list now -->" + getDataResponseList.get(0));
        System.out.println("------------------First Account details after deletion------------------------");
        System.out.println("The Account detail is " + getDataResponseList.get(0));
        Assert.assertEquals(data.getStatusCode(), HttpStatus.SC_OK, "Response code is not matching for get data.");
        report.logTestInfo("Status Code" + data.getStatusLine());
        if (returnTheMatchingGetDataResponse(accountno, userid, getDataResponseList) != null) {
            Assert.fail("Deleted data is still available in the get data response");
           report.logTestFailed("Deleted data is still available in the get data response");
        }
    }

    public GetDataResponse returnTheMatchingGetDataResponse(String accountNo, String userId, List<GetDataResponse> getDataResponseList) {
        for (GetDataResponse dataResponse : getDataResponseList) {
            if (dataResponse.getAccountno().equals(accountNo) && dataResponse.getUserid().equals(userId))
                return dataResponse;
        }
        return null;
    }

}
