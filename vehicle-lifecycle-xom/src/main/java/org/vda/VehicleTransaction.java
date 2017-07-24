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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// SME can't make it work in the RES
// Error when extracting the ruleset parameter value from the request.\nInvalid type id 'org.vda.PrivateVehicleTransfer' (for id type 'Id.class'): no such class found
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIgnoreProperties({"$class", "vehicle"})
public class VehicleTransaction 
{
 	public String timestamp;
 	public String transactionId;
	public Vehicle vehicle;

}
