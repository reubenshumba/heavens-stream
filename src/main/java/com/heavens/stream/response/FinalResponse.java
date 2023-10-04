package com.heavens.stream.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FinalResponse {
	
	private Boolean success;

	private Integer statusCode;

	private String message;
	
	private Object data;
}
