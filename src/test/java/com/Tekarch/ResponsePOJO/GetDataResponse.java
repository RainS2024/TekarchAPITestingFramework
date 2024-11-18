package com.Tekarch.ResponsePOJO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetDataResponse {
	@JsonProperty(value = "accountno")
	private String accountno;
	@JsonProperty(value = "departmentno")
	private String departmentno;
	@JsonProperty(value = "salary")
	private String salary;
	@JsonProperty(value = "pincode")
	private String pincode;
	@JsonProperty(value = "userid")
	private String userid;
	@JsonProperty(value = "id")
	private String id;
	
	
}
