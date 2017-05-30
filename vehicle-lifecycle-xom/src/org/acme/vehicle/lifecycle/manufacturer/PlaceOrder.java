package org.acme.vehicle.lifecycle.manufacturer;

import org.vda.VehicleDetails;
import composer.base.Person;

public class PlaceOrder 
{
	public String transactionId;
	public String orderId;
	public VehicleDetails vehicleDetails;
	public Manufacturer manufacturer;
	public Person orderer;
}
