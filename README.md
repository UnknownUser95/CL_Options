# CL Options

CL Options is a Java library for processing and reacting to command line arguments.

# Usage

Options can be obtained with the `Option.getOption` methods.

Every option has a short and a long name. Any of them can be `null` (for does not have it), but not both of them. It is also not checked whether the long name is actually longer than the short name.

Every option also requires an action, which represent the logic of that option.

The action can access later arguments, as long as requiredNextArguments is not set to 0. If requiredNextArguments is -1, it will pass **all** following arguments. Options can also overlap. Each implementation has to test itself, whether the given arguments are correct.

Optionally options can be set to be required. An exception (`OptionNotGivenException`) will be thrown when a required option does not exist.
