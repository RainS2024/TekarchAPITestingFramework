package com.Tekarch.API.POJO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.Tekarch.RequestPOJO.CreateDataPOJO;
import com.Tekarch.RequestPOJO.DeleteDataPOJO;
import com.Tekarch.RequestPOJO.LoginRequestPOJO;
import com.Tekarch.RequestPOJO.UpdateDataRequestPOJO;
import com.Tekarch.ResponsePOJO.EditDataResponsePOJO;
import com.Tekarch.ResponsePOJO.GetDataResponse;
import com.Tekarch.ResponsePOJO.LoginResponsePOJO;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.*;

public class APIwithPOJO {
private static String Mytoken;	
private static String Createdid;
private static String Createduserid;
RequestSpecification spec1;
	
 @BeforeTest  
public void setup()	{
	 RestAssured.baseURI = "https://us-central1-qa01-tekarch-accmanager.cloudfunctions.net/";
	 RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	//spec1= new RequestSpecBuilder().setContentType(ContentType.JSON).build();
 }
 
public String login() {
	LoginRequestPOJO data = new LoginRequestPOJO();
	data.setUsername("garima.symbiosis@tekarch.com ");
	data.setPassword("Admin123");
	Response res = RestAssured
			.given().contentType(ContentType.JSON)
			.body(data)
			.when().post("login");
	res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/LoginResponse.json"));
	LoginResponsePOJO[] List = res.as(LoginResponsePOJO[].class);
	//System.out.println(List[0].getToken());
	res.prettyPrint();
	Mytoken= List[0].getToken();
	return Mytoken;
	/*List<LoginResponsePOJO> ListResponse= res.as(new TypeRef<List<LoginResponsePOJO>>() {});
	System.out.println(ListResponse.get(0).getToken());
	res.prettyPrint();
	Mytoken= ListResponse.get(0).getToken();
	return Mytoken;*/
	
}

@Test(priority = 0)
public void getdata() {
Header tokenheader = new Header ("token",login());
RequestSpecification req = RestAssured
.given()
.header(tokenheader)
.log().body();
Response response = req.when().get("getdata");
response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/Getdata.json"))
.statusCode(200)
.log().status()
.time(lessThan(15000L));

System.out.println("------------Initial set of data------------------------");
System.out.println("total records="+response.body().jsonPath().get("size()"));
System.out.println("first record account no="+response.body().jsonPath().get("[0].accountno"));
System.out.println("first record dept num="+response.body().jsonPath().get("[0].departmentno"));
System.out.println("first record salary="+response.body().jsonPath().get("[0].salary"));
System.out.println("first record user id="+response.body().jsonPath().get("[0].userid"));
System.out.println("first record id="+response.body().jsonPath().get("[0].id"));
System.out.println("-------------------------------------------------------");
System.out.println("2nd record account no="+response.body().jsonPath().get("[1].accountno"));
System.out.println("2nd record dept num="+response.body().jsonPath().get("[1].departmentno"));
System.out.println("2ndt record salary="+response.body().jsonPath().get("[1].salary"));
System.out.println("2nd record user id="+response.body().jsonPath().get("[1].userid"));
System.out.println("2nd record id="+response.body().jsonPath().get("[1].id"));	
}
@Test (priority=1)
public void createdata() {
	Header tokenheader = new Header ("token",login());
	CreateDataPOJO create = new CreateDataPOJO();
	create.setAccountno("TA-Rain08");
	create.setDepartmentno("5");
	create.setSalary("5000");
	create.setPincode("555111");
	
	RequestSpecification req = RestAssured
	     .given()
         .header(tokenheader)
	     .contentType(ContentType.JSON)
	     .body(create);
	  Response Res = req.when().post("addData");
	  Res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/EditData.json"))
	  .statusCode(201)
	  .contentType(ContentType.JSON);
	  EditDataResponsePOJO statusresponse = Res.as(EditDataResponsePOJO.class);
	  System.out.println("Create Data Status Response -->" + statusresponse.getStatus());
	  Res.prettyPrint();		
	
}
@Test(priority=2)
public void getdataaftercreate() {
Header tokenheader = new Header ("token",login());
RequestSpecification req = RestAssured
.given()
.header(tokenheader);

Response response2 = req
.when().get("getdata");

response2.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/Getdata.json"))
.statusCode(200)
.log().status();

List<GetDataResponse> ListResponse=response2.as(new TypeRef<List<GetDataResponse>>() {});
/*System.out.println(ListResponse.get(0).getAccountno());
System.out.println(ListResponse.get(0).getDepartmentno());
System.out.println(ListResponse.get(0).getSalary());
System.out.println(ListResponse.get(0).getPincode());*/
Createdid=ListResponse.get(0).getId();
Createduserid=ListResponse.get(0).getUserid();
System.out.println("Created ID -->" + Createdid);
System.out.println("Created Userid--->" + Createduserid);


System.out.println("------------After adding data------------------------");
System.out.println("total records="+response2.body().jsonPath().get("size()"));
System.out.println("first record account no="+response2.body().jsonPath().get("[0].accountno"));
System.out.println("first record dept num="+response2.body().jsonPath().get("[0].departmentno"));
System.out.println("first record salary="+response2.body().jsonPath().get("[0].salary"));
System.out.println("first record user id="+response2.body().jsonPath().get("[0].userid"));
System.out.println("first record id="+response2.body().jsonPath().get("[0].id"));
}

@Test(priority=3)
public void updatedata() {
	
	Header tokenheader = new Header ("token",login());
	UpdateDataRequestPOJO update = UpdateDataRequestPOJO.builder()
			.accountno("TA-Rain08")
			.departmentno("1")
			.salary("5000")
			.pincode("555111")
			.userid(Createduserid)
			.id(Createdid)
			.build(); 
	
	Response response3 = RestAssured
	   .given()
         .header(tokenheader)
	     .contentType(ContentType.JSON)
	     .body(update)     
	 .when().put("updateData");
	 response3.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/EditData.json"))
	 .contentType(ContentType.JSON)
	 .statusCode(200);
	  EditDataResponsePOJO statusresponse = response3.as(EditDataResponsePOJO.class);
	  System.out.println("Update Data Status Response -->" + statusresponse.getStatus());
	  response3.prettyPrint();
		
}

@Test(priority=4)
public void getdataafterupdate() {
Header tokenheader = new Header ("token",login());
Response response4 = RestAssured
.given().header(tokenheader)
.pathParam("Createdidvalue",Createdid)
.when().get("getdata/{Createdidvalue}");
response4.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/Getdata.json"))
.statusCode(200)
.log().status();
//response4.prettyPrint();
List<GetDataResponse> ListResponse=response4.as(new TypeRef<List<GetDataResponse>>() {});
System.out.println("------------After updating data------------------------");
System.out.println(ListResponse.get(0).getAccountno());
System.out.println(ListResponse.get(0).getDepartmentno());
System.out.println(ListResponse.get(0).getSalary());
System.out.println(ListResponse.get(0).getUserid());
System.out.println(ListResponse.get(0).getId());
System.out.println("------------------------------------");

System.out.println("updates data " + response4.body().jsonPath().getList("findAll{it.id =='Createdid'}"));


}


@Test(priority=5)
public void deletedata() {
	Header tokenheader = new Header ("token",login());
	DeleteDataPOJO delete = DeleteDataPOJO.builder()
			.userid(Createduserid)
			.id(Createdid)
			.build();
	
	Response response5 = RestAssured
	   .given()
         .header(tokenheader)
	     .contentType(ContentType.JSON)
	     .body(delete)
	    
	.when().delete("deleteData");
	response5.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/EditData.json"));
	response5.then()
	.statusCode(200)
	.contentType(ContentType.JSON);
	response5.prettyPrint();	
	
}
@Test(priority=6)
public void getdataafterdeletion() {
Header tokenheader = new Header ("token",login());
Response response6 = RestAssured
.given().header(tokenheader)
.when().get("getdata");
response6.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ExpectedJsonSchema/Getdata.json"));
response6.then().statusCode(200)
.log().status();
System.out.println("------------After deleting data------------------------");
System.out.println("total records="+response6.body().jsonPath().get("size()"));
System.out.println("first record account no="+response6.body().jsonPath().get("[0].accountno"));
System.out.println("first record dept num="+response6.body().jsonPath().get("[0].departmentno"));
System.out.println("first record salary="+response6.body().jsonPath().get("[0].salary"));
System.out.println("first record user id="+response6.body().jsonPath().get("[0].userid"));
System.out.println("first record id="+response6.body().jsonPath().get("[0].id"));

}

}

