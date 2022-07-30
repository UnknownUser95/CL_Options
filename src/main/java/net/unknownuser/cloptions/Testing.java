package net.unknownuser.cloptions;

import java.util.*;
import static net.unknownuser.cloptions.Option.*;

public class Testing {
	public static void main(String[] args) {
		Testing testInstance = new Testing();
		
		// both of these will function
		// newArgs1 is likely what is wanted, but newArgs2 may be also wanted
		// the implementation must test itself, whether the given arguments are correct
		// (the - or -- before any option is not needed, but I like it more)
		String[] newArgs1 = {"-s", "test1", "test2", "test3", "--output", "test4", "-o2", "test5", "-o3", "test6", "-o4", "test7"};
		String[] newArgs2 = {"-s", "test1", "--output", "test4", "-o2", "test5", "-o3", "test6", "-o4", "test7"};
		
		// OptionAction can be instantiated for later use
		// the normal form is also functional, but the lambda is much shorter
		OptionAction action = list -> {
			System.out.println("printing contents of list, but from an OptionAction instance:");
			list.forEach(System.out::println);
			System.out.println("done");
		};
		
		// using function references like this is highly recommended, but not a required
		CL_Options.addOption(getOption("-s", null, System.out::println, 3, true));
		CL_Options.addOption(getOption("-o", "--output", Testing::printList, 1, false));
		CL_Options.addOption(getOption("-o2", null, testInstance::instancePrintList, 1, false));
		
		// alternatively an already created OptionAction should be given
		CL_Options.addOption(getOption("-o3", null, action, 1, false));
		
		// a lambda can also be created during creation, but this is not particularly clear about what is happening
		CL_Options.addOption(getOption("-o4", null, list -> {
			System.out.println("printing contents of list, but from a lambda:");
			list.forEach(System.out::println);
			System.out.println("done");
		}, 1, false));
		
		// the options are applied on any String[]
		CL_Options.apply(newArgs1);
		System.out.println();
		CL_Options.apply(newArgs2);
	}
	
	// function references are a short form of showing what an option does
	// as long as the argument is (List<String> [any name]), the function will work
	
	// a static function
	public static void printList(List<String> list) {
		System.out.println("printing contents of list:");
		list.forEach(System.out::println);
		System.out.println("done");
	}
	
	// an object bound function
	public void instancePrintList(List<String> list) {
		System.out.println("printing contents of list, but from a Testing instance:");
		list.forEach(System.out::println);
		System.out.println("done");
	}
}
