package mycompany.nzr.dc;

public class DataCaptureException extends Exception {

	private String message;
	private Throwable cause;
	
	private static final long serialVersionUID = 1L;
	
	public DataCaptureException(String message) {
		this.message = message;
	}

	public DataCaptureException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;	
	}

	@Override
	public Throwable getCause() {
		return this.cause;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
