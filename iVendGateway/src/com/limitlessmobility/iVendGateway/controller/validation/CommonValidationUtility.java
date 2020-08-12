package com.limitlessmobility.iVendGateway.controller.validation;

import java.util.Collection;
import java.util.Map;

/**
 * @author RamN
 *
 */
public class CommonValidationUtility {

	private CommonValidationUtility() {
		
	}

	/**
	 * Checks if is empty.
	 *
	 * @param value the value
	 * @return true, if is empty
	 */
	public static boolean isEmpty( Object value ){
		if( value == null ){
			return true;
		}
		else if( value instanceof String ){
			return ( (String) value ).trim().length() == 0;
		}
		else if( value instanceof Object[] ){
			return ( (Object[]) value ).length == 0;
		}
		else if( value instanceof Collection<?> ){
			return ( (Collection<?>) value ).isEmpty();
		}
		else if( value instanceof Map<?, ?> ){
			return ( (Map<?, ?>) value ).size() == 0;
		}
		else{
			return value.toString() == null || value.toString().trim().length() == 0;
		}
	}

	
	/**
	 * Checks if is numeric.
	 *
	 * @param str the str
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
}
