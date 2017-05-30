package org.vda;

import java.util.ArrayList;

import composer.base.Person;

public class Vehicle 
{
	public String vin;
	public VehicleDetails vehicleDetails;
	public VehicleStatus vehicleStatus;
	public Person owner ;
	public String numberPlate ;
	public String suspiciousMessage ;
	public ArrayList<VehicleTransferLogEntry> logEntries ;
}
