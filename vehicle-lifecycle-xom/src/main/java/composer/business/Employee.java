package composer.business;

import java.util.Date;

import composer.base.Person;

public class Employee extends Person 
{
	public String $class;
	public Business employer;
	public Manager manager ;
	public Date startDate ;
	public String employmentStatus ;
	public String department ;
	public String jobRole ;
}
