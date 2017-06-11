package composer.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"$class"})
public enum Gender {
	MALE ,
	FEMALE,
	OTHER
}
