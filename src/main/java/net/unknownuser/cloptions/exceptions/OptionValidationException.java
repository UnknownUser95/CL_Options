package net.unknownuser.cloptions.exceptions;

public class OptionValidationException extends RuntimeException {
	private static final long serialVersionUID = 4266599134756552126L;

	protected OptionValidationException(String str) {
		super(str);
	}
}
