package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 5127369429202725869L;

	public OptionAlreadyExistsException() {
		super("the given option conflicts with an already added option");
	}
	
	public OptionAlreadyExistsException(Option option) {
		super(String.format("the option (%s) conflicts with an already added option", option.toString()));
	}
	
	public OptionAlreadyExistsException(Option newOption, Option oldOption) {
		super(String.format("the new option (%s) conflicts with an already added option (%s)", newOption.toString(), oldOption.toString()));
	}
}
