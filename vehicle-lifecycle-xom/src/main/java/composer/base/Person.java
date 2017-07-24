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

package composer.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.vda.Vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ssn")
public  class Person 
{
	/*
	public static Map<Long,ThreadLocal<Map<String, Person>>> PERSON_THREADMAP;
	
	static {
		PERSON_THREADMAP = new HashMap<Long,ThreadLocal<Map<String, Person>>>();
	}
	
	public static Map<String, Person> getPersonMap() 
	{
		long id = Thread.currentThread().getId();
		Map<String, Person> result = null;
		ThreadLocal<Map<String, Person>> tl = PERSON_THREADMAP.get(id);
		if (tl != null) {
			result = tl.get();
		}		
		if (result == null) {
			result = new HashMap<String, Person>();			
			PERSON_THREADMAP.put(id, new ThreadLocal<Map<String, Person>>());
			PERSON_THREADMAP.get(id).set(result);
		}
		return result;
	}
	
	public static Person getPerson(String ssn) 
	{
		// System.out.println("--------> get person (thread id:" + Thread.currentThread().getId() + "): " + ssn);
		// return getPersonMap().get(ssn);
		return null;
	}
	
	public static void clearPersons() 
	{
		// getPersonMap().clear();
		// PERSON_THREADMAP.remove(Thread.currentThread().getId());
		// System.out.println("clear persons (thread id:" + Thread.currentThread().getId() + ")");
	}
	*/

	public Person() {
		// TODO Auto-generated constructor stub
	}

	public Person(String ssn) {
		this.ssn = ssn;
	}

	public String ssn ;
	
	@JsonProperty("ssn")
	public void setSsn(String ssn) {
		this.ssn = ssn;
		// System.out.println("--------> creating a new person (thread id:" + Thread.currentThread().getId() + "): " + ssn);
		// getPersonMap().put(ssn, this);
	}
	
	public String title ;
	public String firstName ;  
	public String lastName ;  
	public ArrayList<String> middleNames  ; 
	public Gender gender  ;
	public ArrayList<String> nationalities  ;
	public ContactDetails contactDetails  ;
	public BirthDetails birthDetails  ;
	public DeathDetails deathDetails  ;
}
