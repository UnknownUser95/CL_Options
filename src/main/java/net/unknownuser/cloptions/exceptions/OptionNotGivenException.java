package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionNotGivenException extends OptionValidationException {
	private static final long serialVersionUID = 4365307472366347708L;

	public OptionNotGivenException() {
		super("required options not satisfied");
	}
	
	public OptionNotGivenException(Option opt) {
		super(String.format("the option (%s) is not satisfied", opt));
	}
}
