package org.vda;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
public class VehicleTransaction 
{
 	public String transactionId;
	public Vehicle vehicle;

}
