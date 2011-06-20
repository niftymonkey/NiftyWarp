package net.niftymonkey.niftywarp.exceptions;

/**
 * User: Mark Lozano
 * Date: 6/20/11
 * Time: 1:58 AM
 */
public class InternalPermissionsException extends Exception
{
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
