package net.unknownuser.cloptions;

import java.util.*;

import net.unknownuser.cloptions.exceptions.*;
import static net.unknownuser.cloptions.Option.*;

public abstract class CL_Options {
	private static ArrayList<Option> options = new ArrayList<>();
	private static int minArgLength = 0;
	
	/**
	 * Adds an option to the option list.
	 * 
	 * @param option The new option.
	 * @return {@code true} if the option was added, {@code false} otherwise.
	 */
	public static void addOption(Option option) {
		// check, if any conflict exists
		for(Option opt : options) {
			if(stringsMatch(opt.shortName, option.shortName) || stringsMatch(opt.longName, option.longName)) {
				throw new OptionAlreadyExistsException(option, opt);
			}
		}
		
		if(option.required) {
			if(option.allowOverlap) {
				// arguments are not included
				minArgLength += 1;
			} else {
				// arg length and the arg itself
				minArgLength += option.requiredNextOptions + 1;
			}
		}
		
		options.add(option);
	}
	
	/**
	 * Runs all options with the given arguments.
	 * 
	 * @param args The command line arguments.
	 * @param allowOverlap Whether option overlap should be checked.
	 * @return An {@link java.util.Optional Optional} containing an error. If it's empty no error occurred.
	 */
	public static Optional<Exception> apply(String[] args) {
		if(args.length < minArgLength) {
			return Optional.of(new TooFewArgumentsException());
		}
		
		ArrayList<String> argsList = new ArrayList<>();
		argsList.addAll(Arrays.asList(args));
		
		// index of last required argument of last option
		TypeHolder<Integer> lastArgumentIndex = new TypeHolder<>(-1);
		
		// validation
		for(Option opt : options) {
			int shortIndex = argsList.indexOf(opt.shortName);
			int longIndex = argsList.indexOf(opt.longName);
			// both long and short version are present
			if(shortIndex != -1 && longIndex != -1) {
				return Optional.of(new OptionAlreadyGivenException(opt));
			}
			int shortLastIndex = argsList.lastIndexOf(opt.shortName);
			int longLastIndex = argsList.lastIndexOf(opt.longName);
			if(shortIndex != shortLastIndex || longIndex != longLastIndex) {
				return Optional.of(new OptionAlreadyGivenException(opt));
			}
			
			// cut down long and short index to one
			int index = (shortIndex != -1) ? shortIndex : longIndex;
			if(index == -1) {
				if(opt.required) {
					// required option must exist
					return Optional.of(new OptionNotGivenException(opt));
				} else {
					// optional option doesn't exist
					continue;
				}
			}
			
			// current option is in the argument list of the last option
			if(!opt.allowOverlap && index <= lastArgumentIndex.get()) {
				return Optional.of(new OptionsOverlapException(opt));
			}
			
			// not enough space for needed arguments
			if((index + opt.requiredNextOptions) >= argsList.size()) {
				return Optional.of(new TooFewArgumentsException());
			}
			
			lastArgumentIndex.set(index + opt.requiredNextOptions);
		}
		
		// apply options
		for(Option opt : options) {
			int shortIndex = argsList.indexOf(opt.shortName);
			int longIndex = argsList.indexOf(opt.longName);
			int index = (shortIndex != -1) ? shortIndex : longIndex;
			
			// optional option, which was not given
			if(index == -1) {
				continue;
			}
			
			if(opt.requiredNextOptions == -1) {
				// pass all other arguments
				opt.action.apply(argsList.subList(index + 1, argsList.size()));
			} else {
				// pass required options
				opt.action.apply(argsList.subList(index + 1, index + opt.requiredNextOptions + 1));
			}
		}
		
		return Optional.ofNullable(null);
	}
}
