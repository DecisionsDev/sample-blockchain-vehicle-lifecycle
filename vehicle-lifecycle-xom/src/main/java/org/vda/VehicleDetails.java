/*
 *
 *   Copyright IBM Corp. 2017
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.vda;

public class VehicleDetails 
{
	public String $class;
	  public String make;
	  public String modelType;
	  public String colour;
	  public String vin ;
	  public String modelVariant ;
	  public String modelVersion ;
	  public String bodyType ;
	  public TaxClass taxationClass ;
	  public Integer revenueWeight ;
	  public Integer cylinderCapacity ;
	  public Double co2 ; // g/km
	  public String typeOfFuel ;
	  public Integer numberOfSeats ;
	  public Integer numberOfStandingPlaces ;
	  public String wheelPlan ;
	  public String vehicleCategory ;
	  public String typeApprovalNumber ;
	  public Double maxNetPower ; // kW
	  public String engineNumber ;
	  public Double maxPermissibleMass ;
	  public Double massInService ;
	  public Double powerWeightRatio ;
	  public TrailerDetails trailerDetails ;
	  public SoundDetails soundDetails ;
	  public ExhaustEmissions exhaustEmissions ;

}
