package org.vda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// SME can't make it work in the RES
// Error when extracting the ruleset parameter value from the request.\nInvalid type id 'org.vda.PrivateVehicleTransfer' (for id type 'Id.class'): no such class found
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIgnoreProperties({"$class"})
public class VehicleTransaction 
{
 	public String timestamp;
 	public String transactionId;
	public Vehicle vehicle;

}
