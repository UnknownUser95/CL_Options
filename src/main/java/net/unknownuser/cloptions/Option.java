package net.unknownuser.cloptions;

import java.security.*;
import java.util.*;

/**
 * An option for processing command line arguments.
 */
public class Option {
	public final String shortName;
	public final String longName;
	public final OptionAction action;
	public final boolean required;
	public final boolean allowOverlap;
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
	 * @param longName            The long name (e.g. {@code "--output"}) of this option.
	 * @param action              The action this option will do.
	 * @param requiredNextOptions The amount of following arguments this option requires.
	 * @param required            Whether this option is required or can be omitted.
	 * @param allowOverlap        Whether this option can overlap with another.
	 */
	protected Option(String shortName, String longName, OptionAction action, int requiredNextOptions, boolean required, boolean allowOverlap) {
		super();
		this.shortName = shortName;
		this.longName = longName;
		this.action = action;
		this.requiredNextOptions = requiredNextOptions;
		this.required = required;
		this.allowOverlap = allowOverlap;
	}
	
	/**
	 * Creates an option with the specified parameters.
	 * 
	 * @param shortName           The short name (e.g. {@code "-o"}) of this option.
	 * @param longName            The long name (e.g. {@code "--output"}) of this option.
	 * @param action              The action this option will do.
	 * @param requiredNextOptions The amount of following arguments this option requires.
	 * @param required            Whether this option is required or can be omitted.
	 * @param allowOverlap        Whether this option can overlap with other options.
	 * @return An option with the given arguments.
	 */
	public static Option getOption(String shortName, String longName, OptionAction action, int requiredNextOptions, boolean required, boolean allowOverlap) {
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
		
		return new Option(shortName, longName, action, requiredNextOptions, required, allowOverlap);
	}
	
	/**
	 * Creates an option with the specified parameters.<br>
	 * The resulting option is not required and does not need any following arguments.
	 * 
	 * @param shortName The short name (e.g. {@code "-o"}) of this option.
	 * @param longName  The long name (e.g. {@code "--output"}) of this option.
	 * @param action    The action this option will do.
	 * @return An option with the given arguments.
	 */
	public static Option getOption(String shortName, String longName, OptionAction action) {
		return getOption(shortName, longName, action, 0, false, false);
	}
	
	@Override
	public String toString() {
		return String.format("Option{shortName:\"%s\", longName:\"%s\", required:%b, allowOverlap:%b}", shortName, longName, required, allowOverlap);
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, longName, required, requiredNextOptions, shortName);
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
			// action is left out
			return nullableStringsMatch(shortName, opt.shortName) && nullableStringsMatch(longName, opt.longName) && required == opt.required;
		}
		
		return false;
	}
	
	protected static boolean stringsMatch(String str1, String str2) {
		if(str1 == null || str2 == null) {
			return false;
		}
		
		return str1.equals(str2);
	}
	
	protected static boolean nullableStringsMatch(String str1, String str2) {
		if(str1 == null && str2 == null) {
			// both are null
			return true;
		}
		
		if(str1 == null || str2 == null) {
			// one is null, the other is not
			return false;
		}
		
		// both aren't null
		return str1.equals(str2);
	}
}
