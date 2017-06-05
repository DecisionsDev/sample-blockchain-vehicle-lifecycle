package composer.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vda.Vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;


public  class Person 
{
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
		System.out.println("--------> get person (thread id:" + Thread.currentThread().getId() + "): " + ssn);
		return getPersonMap().get(ssn);
	}
	
	public static void clearPersons() 
	{
		getPersonMap().clear();
		PERSON_THREADMAP.remove(Thread.currentThread().getId());
		System.out.println("clear persons (thread id:" + Thread.currentThread().getId() + ")");
	}

	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	public String $class;
	public String ssn ;
	
	@JsonProperty("ssn")
	public void setSsn(String ssn) {
		this.ssn = ssn;
		System.out.println("--------> creating a new person (thread id:" + Thread.currentThread().getId() + "): " + ssn);
		getPersonMap().put(ssn, this);
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
