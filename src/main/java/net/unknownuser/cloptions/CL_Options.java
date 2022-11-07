package net.unknownuser.cloptions;

import java.util.*;

import net.unknownuser.cloptions.exceptions.*;

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
			// names have to match and
			// both don't allow duplicates
			if(opt.nameMatch(option) && !(opt.allowDuplicates && option.allowDuplicates)) {
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
	
	public enum ApplyStatus {
		/**
		 * Either there are too few arguments overall or one option doesn't have enough arguments.
		 */
		TOO_FEW_ARGUMENTS(new TooFewArgumentsException()),
		/**
		 * A required option was not given.
		 */
		REQUIRED_OPTION_NOT_GIVEN(new OptionNotGivenException()),
		/**
		 * An option was given at least twice.
		 */
		OPTION_ALREADY_GIVEN(new OptionAlreadyGivenException()),
		/**
		 * At least two options overlap.
		 */
		OPTION_OVERLAP(new OptionsOverlapException()),
		/**
		 * Finished without errors.
		 */
		FINISHED(null);
		
		private Exception exc;
		
		private ApplyStatus(Exception exc) {
			this.exc = exc;
		}
		
		public Exception get() {
			return exc;
		}
	}
	
	/**
	 * Runs all options with the given arguments.
	 * 
	 * @param args The command line arguments.
	 * @return A {@link ApplyStatus Status}. {@link ApplyStatus#FINISHED FINISHED} when no error
	 *         occurred. The other types are returned when their specific error occurred.
	 */
	public static ApplyStatus apply(String[] args) {
		if(args.length < minArgLength) {
			return ApplyStatus.TOO_FEW_ARGUMENTS;
		}
		
		ArrayList<String> argsList = new ArrayList<>();
		argsList.addAll(Arrays.asList(args));
		
		// index of last required argument of last option
//		TypeHolder<Integer> lastIndex = new TypeHolder<>(-1);
		int lastIndex = -1;
		boolean lastAllowedOverlap = false;
		
		// validation
		for(Option opt : options) {
			int shortIndex = argsList.indexOf(opt.shortName);
			int longIndex = argsList.indexOf(opt.longName);
			// both long and short version are present
			if(shortIndex != -1 && longIndex != -1) {
				return ApplyStatus.OPTION_ALREADY_GIVEN;
			}
			int shortLastIndex = argsList.lastIndexOf(opt.shortName);
			int longLastIndex = argsList.lastIndexOf(opt.longName);
			if(shortIndex != shortLastIndex || longIndex != longLastIndex) {
				return ApplyStatus.OPTION_ALREADY_GIVEN;
			}
			
			// cut down long and short index to one
			int index = (shortIndex != -1) ? shortIndex : longIndex;
			if(index == -1) {
				if(opt.required) {
					// required option must exist
					return ApplyStatus.REQUIRED_OPTION_NOT_GIVEN;
				} else {
					// optional option doesn't exist
					continue;
				}
			}
			
			// current option is in the argument list of the last option
			if(index <= lastIndex && !lastAllowedOverlap) {
				return ApplyStatus.OPTION_OVERLAP;
			}
			
			// not enough space for needed arguments
			if((index + opt.requiredNextOptions) >= argsList.size()) {
				return ApplyStatus.TOO_FEW_ARGUMENTS;
			}
			
			lastIndex = index + opt.requiredNextOptions;
			lastAllowedOverlap = opt.allowOverlap;
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
				opt.action.accept(argsList.subList(index + 1, argsList.size()));
			} else {
				// pass required options
				opt.action.accept(argsList.subList(index + 1, index + opt.requiredNextOptions + 1));
			}
		}
		
		return ApplyStatus.FINISHED;
	}
}
