package vrt.exception;

/**
 * if the creation of a cv is failed this exception will be thrown
 * 
 * @author sony
 * 
 */
public class VrtResultException extends Exception {

	/**
	 * 
	 */
	public VrtResultException() {
	}

	/**
	 * @param message
	 */
	public VrtResultException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public VrtResultException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VrtResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
