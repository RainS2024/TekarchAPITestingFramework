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
public class UpdateDataRequestPOJO {
	    @JsonProperty(value = "pincode")
	    public String pincode;
	    @JsonProperty(value = "salary")
	    public String salary;
	    @JsonProperty(value = "accountno")
	    public String accountno;
	    @JsonProperty(value = "departmentno")
	    public String departmentno;
	    @JsonProperty(value = "userid")
	    public String userid;
	    @JsonProperty(value = "id")
	    public String id;
	
}
