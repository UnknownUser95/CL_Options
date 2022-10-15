package net.unknownuser.cloptions.exceptions;

public class OptionsOverlapException extends RuntimeException {
	private static final long serialVersionUID = 8833419839366334066L;

	public OptionsOverlapException() {
		super("The given options overlap at least once.");
	}
}
