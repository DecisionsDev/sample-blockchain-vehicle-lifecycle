package org.vda;

import java.util.Date;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import composer.base.Person;

@JsonDeserialize(using = VehicleTransferLogEntryDeserializer.class)
public class VehicleTransferLogEntry 
{
	public VehicleTransferLogEntry() {
	}
	public String $class;
	public Vehicle vehicle;
	public Person buyer;
	public Person seller ;
	public Date timestamp;
}