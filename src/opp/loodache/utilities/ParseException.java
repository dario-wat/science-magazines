package opp.loodache.utilities;

/**
 * Iznimka pri parsiranju datoteka.
 */
public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseException() {
		super();
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
