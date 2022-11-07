package net.unknownuser.cloptions;

import java.util.*;
import java.util.function.*;

import static net.unknownuser.cloptions.Option.*;

public class Testing {
	public static void main(String[] args) {
		Testing testInstance = new Testing();
		
		// both of these will function
		// newArgs1 is likely what is wanted, but newArgs2 may be also wanted
		// the implementation must test itself, whether the given arguments are correct
		// (the - or -- before any option is not needed, but I like it more)
		String[] newArgs1 = { "blocker1", "blocker2", "-s", "test1", "test2", "test3", "--output", "test4", "-o2", "test5", "-o3", "test6", "-o4", "test7" };
		String[] newArgs2 = { "-s", "test1", "--output", "test4", "-o2", "test5", "-o3", "test6", "-o4", "test7" };
		
		// OptionAction can be instantiated for later use
		// the normal form is also functional, but the lambda is much shorter
		Consumer<List<String>> action = list -> {
			System.out.println("printing contents of list, but from an OptionAction instance:");
			list.forEach(System.out::println);
			System.out.println("done");
		};
		
		// using function references like this is recommended, but not a required
		CL_Options.addOption(getOption("-s", null, System.out::println, 3, true, false, false)); // allowOverlap decides if the second arguments are valid
		CL_Options.addOption(getOption("-o", "--output", Testing::staticPrintList, 1, false, false, false));
		CL_Options.addOption(getOption("-o2", null, testInstance::instancePrintList, 1, false, false, false));
		
		// alternatively an already created OptionAction should be given
		CL_Options.addOption(getOption("-o3", null, action, 1, false, false, false));
		
		// a lambda can also be created during creation, but this is not particularly clear about what is happening
		CL_Options.addOption(getOption("-o4", null, list -> {
			System.out.println("printing contents of list, but from a lambda:");
			list.forEach(System.out::println);
			System.out.println("done");
		}, 1, false, false, false));
		
		// the options are applied on any String[]
		System.out.println("first argument array:");
		// no error means .get() returns null
		System.out.println(CL_Options.apply(newArgs1).get());
		System.out.println();
		System.out.println("second argument array:");
		// example for handling returns
		System.out.println(switch (CL_Options.apply(newArgs2)) {
		case FINISHED -> "finished without errors";
		case OPTION_ALREADY_GIVEN -> "an option was already given";
		case OPTION_OVERLAP -> "some options overlap";
		case REQUIRED_OPTION_NOT_GIVEN -> "a required option was not given";
		case TOO_FEW_ARGUMENTS -> "too few arguments for at least one option";
		});
	}
	
	// function references are a short form of showing what an option does
	// as long as the argument is (List<String> [any name]), the function will work
	
	// a static function
	public static void staticPrintList(List<String> list) {
		System.out.println("printing contents of list, static version:");
		list.forEach(System.out::println);
		System.out.println("done");
	}
	
	// an object bound function
	public void instancePrintList(List<String> list) {
		System.out.println("printing contents of list, instance version:");
		list.forEach(System.out::println);
		System.out.println("done");
	}
}
