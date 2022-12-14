package net.unknownuser.cloptions.exceptions;

public class TooFewArgumentsException extends OptionValidationException {
	private static final long serialVersionUID = 1243677307164634436L;

	public TooFewArgumentsException() {
		super("Too few arguments given");
	}
	
	public TooFewArgumentsException(int needed, int given) {
		super(String.format("Too few arguments given: %d expected, %d available", needed, given));
	}
}
