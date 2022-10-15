package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionAlreadyGivenException extends RuntimeException {
	private static final long serialVersionUID = 23888687755009242L;
	
	public OptionAlreadyGivenException() {
		super("An option has already been given.");
	}

	public OptionAlreadyGivenException(Option option) {
		super(String.format("The option (%s) is already given already", option.toString()));
	}
}
