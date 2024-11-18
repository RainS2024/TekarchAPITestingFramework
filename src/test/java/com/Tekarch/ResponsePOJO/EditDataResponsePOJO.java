package com.Tekarch.ResponsePOJO;

import com.Tekarch.ResponsePOJO.EditDataResponsePOJO;

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
public class EditDataResponsePOJO {
@JsonProperty(value = "status")
private String status;







}
