package net.unknownuser.cloptions;

import static net.unknownuser.cloptions.CL_Options.*;

import java.security.*;

/**
 * An option for processing command line arguments.
 */
public class Option {
	public final String shortName;
	public final String longName;
	public final OptionAction action;
	public final boolean required;
	/**
	 * The following arguments passed to it in the this.action.apply() method.<br>
	 * -1 for all following arguments.<br>
	 * <b>Options can overlap!</b>
	 */
	public final int requiredNextOptions;
	
	/**
	 * Creates an option with the specified parameters.
	 * 
	 * @param shortName           The short name (e.g. {@code "-o"}) of this option.
	 * @param longName            The long name (e.g. {@code --output}) of this option.
	 * @param action              The action this option will do.
	 * @param requiredNextOptions The amount of following arguments this option requires.
	 * @param required            Whether this option is required or can be omitted.
	 */
	protected Option(String shortName, String longName, OptionAction action, int requiredNextOptions, boolean required) {
		super();
		this.shortName = shortName;
		this.longName = longName;
		this.action = action;
		this.requiredNextOptions = requiredNextOptions;
		this.required = required;
	}
	
	/**
	 * Creates an option with the specified parameters.
	 * 
	 * @param shortName           The short name (e.g. {@code "-o"}) of this option.
	 * @param longName            The long name (e.g. {@code --output}) of this option.
	 * @param action              The action this option will do.
	 * @param requiredNextOptions The amount of following arguments this option requires.
	 * @param required            Whether this option is required or can be omitted.
	 * @return An option with the given arguments.
	 */
	public static Option getOption(String shortName, String longName, OptionAction action, int requiredNextOptions, boolean required) {
		if(requiredNextOptions < -1) {
			// everything under -1 is invalid
			throw new InvalidParameterException(String.format("%d is not a valid amount", requiredNextOptions));
		}
		
		if(shortName == null && longName == null) {
			throw new InvalidParameterException("no option name given");
		}
		
		if(action == null) {
			throw new InvalidParameterException("no action given");
		}
		
		return new Option(shortName, longName, action, requiredNextOptions, required);
	}
	
	/**
	 * Creates an option with the specified parameters.<br>
	 * The resulting option is not required.
	 * 
	 * @param shortName           The short name (e.g. {@code "-o"}) of this option.
	 * @param longName            The long name (e.g. {@code --output}) of this option.
	 * @param action              The action this option will do.
	 * @param requiredNextOptions The amount of following arguments this option requires.
	 * @return An option with the given arguments.
	 */
	public static Option getOption(String shortName, String longName, OptionAction action, int requiredNextOptions) {
		return getOption(shortName, longName, action, requiredNextOptions, false);
	}
	
	/**
	 * Creates an option with the specified parameters.<br>
	 * The resulting option is not required and does not need any following arguments.
	 * 
	 * @param shortName The short name (e.g. {@code "-o"}) of this option.
	 * @param longName  The long name (e.g. {@code --output}) of this option.
	 * @param action    The action this option will do.
	 * @return An option with the given arguments.
	 */
	public static Option getOption(String shortName, String longName, OptionAction action) {
		return getOption(shortName, longName, action, 0, false);
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\" or \"%s\", is %srequired", shortName, longName, (required ? "" : "not "));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(obj == this) {
			return true;
		}
		
		if(obj instanceof Option opt) {
			// testing the action is impossible
			
			return nullableStringsMatch(shortName, opt.shortName) && nullableStringsMatch(longName, opt.longName) && required == opt.required;
		}
		
		return false;
	}
}
