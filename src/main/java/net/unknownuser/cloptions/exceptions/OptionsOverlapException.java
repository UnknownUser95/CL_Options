package net.unknownuser.cloptions.exceptions;

import net.unknownuser.cloptions.*;

public class OptionsOverlapException extends OptionValidationException {
	private static final long serialVersionUID = 2250150824622461298L;

	public OptionsOverlapException() {
		super("At least one option overlaps with another");
	}
	
	public OptionsOverlapException(Option opt) {
		super(String.format("The option %s overlaps with another", opt));
	}
}
