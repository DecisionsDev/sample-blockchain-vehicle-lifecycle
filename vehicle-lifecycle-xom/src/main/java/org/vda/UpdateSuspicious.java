package org.vda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public class UpdateSuspicious extends VehicleTransaction 
{
	public String message;
}
