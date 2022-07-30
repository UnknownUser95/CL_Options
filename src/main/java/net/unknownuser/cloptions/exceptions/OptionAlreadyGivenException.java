package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionAlreadyGivenException extends RuntimeException {
	private static final long serialVersionUID = 23888687755009242L;

	public OptionAlreadyGivenException(Option option) {
		super(String.format("the option (%s) is already given before", option.toString()));
	}
}
