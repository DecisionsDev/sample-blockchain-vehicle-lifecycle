package composer.base;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public  class Person 
{
	public static List<Person> PERSONS = new ArrayList<Person>();
	
	public static Person getPerson(String ssn) 
	{
		for (Person person : PERSONS) {
			if (ssn != null && ssn.compareTo(person.ssn) == 0)
				return person;
		}
		return null;
	}
	
	public static void clearPersons() 
	{
		PERSONS.clear();
	}

	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	public String $class;
	public String ssn ;
	
	@JsonProperty("ssn")
	public void setSsn(String ssn) {
		this.ssn = ssn;
		System.out.println("--------> creating a new person: " + ssn);
		PERSONS.add(this);
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
