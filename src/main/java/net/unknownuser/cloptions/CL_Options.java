package net.unknownuser.cloptions;

import java.util.*;

import net.unknownuser.cloptions.exceptions.*;

public abstract class CL_Options {
	private static ArrayList<Option> options = new ArrayList<>();
	
	/**
	 * Adds an option to the option pool.
	 * @param option The new option.
	 */
	public static void addOption(Option option) {
		// check, if any conflict exists
		options.forEach(opt -> {
			if(stringsMatch(opt.shortName, option.shortName) || stringsMatch(opt.longName, option.longName)) {
				throw new OptionAlreadyExistsException(option, opt);
			}
		});
		
		options.add(option);
	}
	
	/**
	 * Runs all options with the given arguments.
	 * 
	 * @param args The command line arguments.
	 */
	public static void apply(String[] args) {
		ArrayList<String> argsList = new ArrayList<>();
		argsList.addAll(Arrays.asList(args));
		
		for(int i = 0; i < options.size(); i++) {
			Option arg = options.get(i);
			int argIndex = (argsList.indexOf(arg.shortName) != -1) ? argsList.indexOf(arg.shortName) : argsList.indexOf(arg.longName);
			
			if(arg.required && argIndex == -1) {
				// if the option is required, but is not given
				throw new OptionNotGivenException(arg);
			}
			
			int shortIndex = argsList.indexOf(arg.shortName);
			int longIndex = argsList.indexOf(arg.longName);
			if((shortIndex != -1 && longIndex != -1) && (shortIndex != argIndex || longIndex != argIndex)) {
				// the option is already given before
				throw new OptionAlreadyGivenException(arg);
			}
			
			int shortLIndex = argsList.lastIndexOf(arg.shortName);
			int longLIndex = argsList.lastIndexOf(arg.longName);
			if((shortLIndex != -1 && shortLIndex != argIndex) || (longLIndex != -1 && longLIndex != argIndex)) {
				// if any option is given multiple times
				throw new OptionAlreadyGivenException(arg);
			}
			
			if(arg.requiredNextOptions != -1 && argIndex + arg.requiredNextOptions >= argsList.size()) {
				// if the required options are longer than the given array
				throw new TooFewArgumentsException(arg.requiredNextOptions, argsList.size() - argIndex - 1);
			}
		}
		
		// once verified, activate all options
		for(int i = 0; i < options.size(); i++) {
			Option arg = options.get(i);
			int argIndex = (argsList.indexOf(arg.shortName) != -1) ? argsList.indexOf(arg.shortName) : argsList.indexOf(arg.longName);
			
			if(argIndex == -1) {
				// the option is not required and does not exist
				continue;
			}
			
			if(arg.requiredNextOptions == -1) {
				// pass all other arguments
				arg.action.apply(argsList.subList(argIndex + 1, argsList.size()));
			} else {
				arg.action.apply(argsList.subList(argIndex + 1, argIndex + arg.requiredNextOptions + 1));
			}
		}
	}
	
	protected static boolean stringsMatch(String str1, String str2) {
		if(str1 == null || str2 == null) {
			return false;
		}
		
		return str1.equals(str2);
	}
	
	protected static boolean nullableStringsMatch(String str1, String str2) {
		if(str1 != str2) {
			// one is null, the other is not
			return false;
		} else if(str1 == str2) {
			// either: both are null or point to the same location on heap
			return true;
		} else {
			// both aren't null and point to different heap locations
			return str1.equals(str2);
		}
	}
}
