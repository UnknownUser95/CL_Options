package net.unknownuser.cloptions;

import java.util.*;

import net.unknownuser.cloptions.exceptions.*;
import static net.unknownuser.cloptions.Option.*;

public abstract class CL_Options {
	private static ArrayList<Option> options = new ArrayList<>();
	
	/**
	 * Adds an option to the option list.
	 * 
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
		
		for(Option opt : options) {
			int argIndex = (argsList.indexOf(opt.shortName) != -1) ? argsList.indexOf(opt.shortName) : argsList.indexOf(opt.longName);
			
			if(opt.required && argIndex == -1) {
				// if the option is required, but is not given
				throw new OptionNotGivenException(opt);
			}
			
			int shortIndex = argsList.indexOf(opt.shortName);
			int longIndex = argsList.indexOf(opt.longName);
			if((shortIndex != -1 && longIndex != -1) && (shortIndex != argIndex || longIndex != argIndex)) {
				// the option is already given before
				throw new OptionAlreadyGivenException(opt);
			}
			
			int shortLIndex = argsList.lastIndexOf(opt.shortName);
			int longLIndex = argsList.lastIndexOf(opt.longName);
			if((shortLIndex != -1 && shortLIndex != argIndex) || (longLIndex != -1 && longLIndex != argIndex)) {
				// if any option is given multiple times
				throw new OptionAlreadyGivenException(opt);
			}
			
			if(argIndex != -1 && opt.requiredNextOptions != -1 && argIndex + opt.requiredNextOptions >= argsList.size()) {
				// if the required options are longer than the given array
				throw new TooFewArgumentsException(opt.requiredNextOptions, argsList.size() - argIndex - 1);
			}
		}
		
		// once verified, activate all options
		for(Option opt : options) {
			int argIndex = (argsList.indexOf(opt.shortName) != -1) ? argsList.indexOf(opt.shortName) : argsList.indexOf(opt.longName);
			
			if(argIndex == -1) {
				// the option is not required and does not exist
				continue;
			}
			
			if(opt.requiredNextOptions == -1) {
				// pass all other arguments
				opt.action.apply(argsList.subList(argIndex + 1, argsList.size()));
			} else {
				opt.action.apply(argsList.subList(argIndex + 1, argIndex + opt.requiredNextOptions + 1));
			}
		}
	}
}
