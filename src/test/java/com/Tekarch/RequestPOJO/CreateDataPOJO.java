package com.Tekarch.RequestPOJO;

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
public class CreateDataPOJO {
@JsonProperty(value = "accountno")
private String accountno;
@JsonProperty(value = "departmentno")
private String departmentno;
@JsonProperty(value = "salary")
private String salary;
@JsonProperty(value = "pincode")
private String pincode;

}

