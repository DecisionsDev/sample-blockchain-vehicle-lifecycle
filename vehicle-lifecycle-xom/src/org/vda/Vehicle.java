package org.vda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonProperty;

import composer.base.Person;

public class Vehicle 
{
	public static List<Vehicle> VEHICLES = new ArrayList<Vehicle>();
	
	public static Vehicle getVehicle(String vin) 
	{
		for (Vehicle vehicle : VEHICLES) {
			if (vin.compareTo(vehicle.vin) == 0)
				return vehicle;
		}
		return null;
	}
	
	public static void clearVehicles() 
	{
		VEHICLES.clear();
	}
	
	public Vehicle() {
		// TODO Auto-generated constructor stub
	}
	
	public String $class;
	public String vin;
	
	@JsonProperty("vin")
	public void setVin(String v) {
		vin = v;
		System.out.println("--------> creating a new vehicle: " + v);
		VEHICLES.add(this);
	}
	
	public VehicleDetails vehicleDetails;
	public VehicleStatus vehicleStatus;
	public Person owner ;
	public String numberPlate ;
	public String suspiciousMessage ;
	public ArrayList<VehicleTransferLogEntry> logEntries ;
}
