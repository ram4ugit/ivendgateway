package codes;

import java.io.Serializable;

/**
 * @author Ram Narayan Roy
 *
 */
public class ResponseStatusCode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private ResponseStatusCode() {
	}

	
	public static int VALIDATION_FAILED_CODE = 400;
	
	public static String BADREQUEST_CODE = "Bad request !";
	
	public static int UNAUTHORISED_ACCESS = 401;
	
	public static String UNAUTHORISED_ACCESS_MSG = "AccessDenied";
	
	public static String VALIDATION_FAILED_TEXT = "Bad Request";
	
	public static String NO_RECORD = "No Records Found";
	
	public static int OK_CODE = 200;
	
	public static String EXIST_MSG="already exists";
	
	public static String EXIST_NOT_MSG="No Records exists";
	
	public static int SERVER_UNAVAILABLE_CODE = 500;
	
	public static String SERVER_UNAVAILABLE_TEXT = "Server unavailable!";
	
	public static String OK_TEXT = "Success";
	
	public static String FAILURE_TEXT = "Failure";
	
	public static String SAVE_SUCCESS_MSG = "Data saved successfully";
	
	public static int ERROR_CODE = 403;
	
	public static String ERROR_MSG = "FORBIDDEN";
	
	public static String USER_EMAIL_VERIFICATION_FAILED_MESSAGE = "User email verification failed";
	
	
	
}
