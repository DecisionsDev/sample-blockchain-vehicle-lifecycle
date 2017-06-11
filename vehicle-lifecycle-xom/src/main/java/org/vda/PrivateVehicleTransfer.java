package org.vda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import composer.base.Person;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIgnoreProperties({"$class"})
public class PrivateVehicleTransfer extends VehicleTransaction
{
	  public Person seller;
	  public Person buyer;
	  public String specialNotes ;
}
