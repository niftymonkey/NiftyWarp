package net.niftymonkey.niftywarp.exceptions;

/**
 * User: Mark
 * Date: 6/20/11
 * Time: 1:58 AM
 */
public class InternalPermissionsException extends Exception
{
	private static final long serialVersionUID = 1L; //added for consistency, good practice

	public InternalPermissionsException()
    {
        super();
    }

    public InternalPermissionsException(String message)
    {
        super(message);
    }

    public InternalPermissionsException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
