package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionNotGivenException extends RuntimeException {
	private static final long serialVersionUID = 4365307472366347708L;

	public OptionNotGivenException() {
		super("required options not satisfied");
	}
	
	public OptionNotGivenException(Option option) {
		super(String.format("the option (%s) is not satisfied", option.toString()));
	}
}
