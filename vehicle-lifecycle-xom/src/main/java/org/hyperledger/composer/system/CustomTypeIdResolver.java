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
package org.hyperledger.composer.system;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/** 
 * This class is used as a custom type resolver to workaround a classloader issue in the version of
 * Jackson used in RES
 *
 */
public class CustomTypeIdResolver extends TypeIdResolverBase 
{
	private JavaType mBaseType;

	 @Override
	 public void init(JavaType baseType)
	 {
		 mBaseType = baseType;
	 }

	@Override
	public String idFromValue(Object value) {
		return idFromValueAndType(value, value.getClass());
	}

	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		String name = suggestedType.getName();
		return name;
	}
	
    @Override
    public JavaType typeFromId(DatabindContext context, String className)  throws IOException 
    {
        Class<?> clazz = null;
        try {
        	clazz = ClassUtil.findClass(className);
        } catch (ClassNotFoundException e) {
        }
        if (clazz == null) {
        	try {
        	  clazz = Class.forName(className, true, getClass().getClassLoader());
        	} catch (ClassNotFoundException e) {
        	  System.err.println("Cannot find class from CustomTypeIdeResolver: "+ className );
              throw new IllegalStateException("cannot find class '" + className + "'");
        	}
        }
        return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
    }

	@Override
	public Id getMechanism() {
		return Id.CUSTOM;
	}

}
