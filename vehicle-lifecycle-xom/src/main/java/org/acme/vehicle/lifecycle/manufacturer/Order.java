package org.acme.vehicle.lifecycle.manufacturer;

import java.util.ArrayList;

import org.vda.VehicleDetails;

import composer.base.Person;

public class Order 
{
	public String $class;
	public String orderId;
	public VehicleDetails vehicleDetails;
	public OrderStatus orderStatus;
	public Manufacturer manufacturer;
	public Person orderer;
	public ArrayList<UpdateOrderStatus> statusUpdates;
}
