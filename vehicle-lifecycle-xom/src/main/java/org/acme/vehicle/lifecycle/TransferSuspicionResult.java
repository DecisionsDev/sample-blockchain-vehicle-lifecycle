
package org.acme.vehicle.lifecycle;

import org.vda.Vehicle;

import composer.base.Person;

public class TransferSuspicionResult 
{
	public TransferSuspicionResult() 
	{
		// at this point, JSON has been desrialized and we don't need the list
		Vehicle.clearVehicles();
		Person.clearPersons();
	}
	public String $class;
	public String status;
	public String message;
}
