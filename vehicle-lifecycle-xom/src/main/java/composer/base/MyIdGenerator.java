package composer.base;

import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;

public class MyIdGenerator extends PropertyBasedObjectIdGenerator
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyIdGenerator(Class<?> scope) {
		super(scope);
	}
	
	@Override
	public com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey key(
			Object key) {
		// TODO Auto-generated method stub
		// TODO extract id from the fqn id name
		return super.key(key);
	}

}
