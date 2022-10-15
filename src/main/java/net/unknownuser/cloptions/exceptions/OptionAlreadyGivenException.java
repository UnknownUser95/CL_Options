package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionAlreadyGivenException extends OptionValidationException {
	private static final long serialVersionUID = 23888687755009242L;
	
	public OptionAlreadyGivenException() {
		super("An option has already been given.");
	}

	public OptionAlreadyGivenException(Option opt) {
		super(String.format("the option (%s) is already given before", opt));
	}
}
