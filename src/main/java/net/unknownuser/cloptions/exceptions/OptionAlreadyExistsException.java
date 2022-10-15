package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionAlreadyExistsException extends OptionValidationException {
	private static final long serialVersionUID = 5127369429202725869L;

	public OptionAlreadyExistsException() {
		super("The given option conflicts with an already added option");
	}
	
	public OptionAlreadyExistsException(Option opt) {
		super(String.format("the option (%s) conflicts with an already added option", opt));
	}
	
	public OptionAlreadyExistsException(Option newOption, Option oldOption) {
		super(String.format("the new option (%s) conflicts with an already added option (%s)", newOption, oldOption));
	}
}
