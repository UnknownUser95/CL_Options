package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionNotGivenException extends RuntimeException {
	private static final long serialVersionUID = 4365307472366347708L;

	public OptionNotGivenException() {
		super("At least one option doesn't have enough arguments.");
	}
	
	public OptionNotGivenException(Option option) {
		super(String.format("The option (%s) is not satisfied", option.toString()));
	}
}
