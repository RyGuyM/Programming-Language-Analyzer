import java.io.IOException;
import java.util.ArrayList;


public class FLSemantic 
{
	// This method will check the static semantic rules for the program.
	public static boolean statRules(ArrayList<Pair> tokens) throws IOException
	{
		// This array list will hold all the labels found in the entire program.
		ArrayList<String> labelTotal = new ArrayList<String>();
		// This array list will hold all the labels found in a specific subroutine.
		ArrayList<ArrayList<String>> labelCheck = new ArrayList<ArrayList<String>>();
		// This array list will hold all the subroutine names found in the program.
		ArrayList<String> subCheck = new ArrayList<String>();
		// THis array is to hold each of the jump and branch functions for a routine.
		ArrayList<String> jumpCheck = new ArrayList<String>();
		
		// Returns if the static rules are broken or if they are OK.
		boolean correct = true;
		
		// Holds the type of the token currently being analyzed.
		String type;
		// Holds the value of the token currently being analyzed.
		String value;
		// Holds the information of which subroutine is currently being analyzed.
		int subNumber = 0;
		labelCheck.add(new ArrayList<String>());
		
		// Loop through all the tokens and ensure the static rules are met.
		for(int i = 0; i < tokens.size(); i++)
		{
			// Get the token type and value.
			type = tokens.get(i).getToken();
			value = tokens.get(i).getValue();
			
			/* Checks the name of the subroutine to make sure that it has only been defined once
			 * and that it is not the same as any built in functions.
			 */
			if(type.equals("-Sub-"))
			{
				// Check the next value after sub to see if the functions have been defined already
				i++;
				type = tokens.get(i).getToken();
				value = tokens.get(i).getValue();
				
				// If this subroutine has already been defined or it is a built in function then it is an error.
				if(subCheck.contains(value) || value.equals("concat") || value.equals("substr") || value.equals("length") 
						|| value.equals("getchar") || value.equals("putchar") || value.equals("tostring") || value.equals("toint") 
						|| value.equals("tobool") || value.equals("drop") || value.equals("swap") || value.equals("dup") 
						|| value.equals("rot") || value.equals("and") || value.equals("or") || value.equals("not") 
						|| value.equals("save") || value.equals("load") || value.equals("sub") || value.equals("jump") 
						|| value.equals("branch") || value.equals("end") || value.equals("ref"))
				{
					System.out.println("Error: The subroutine " + value + " is already defined in the program or it is a" +
							"built in function.");
					correct = false;
					break;
				}
				// Otherwise add the name of the function to the list.
				else
				{
					subCheck.add(value);
				}
			}
			
			/* Checks the name value of the label to ensure that will label has only been defined
			 * once in the program.
			 */
			else if(type.equals("-Label-"))
			{
				// Check to see if the label has already be defined yet. If it has it is an error.
				if(labelTotal.contains(value))
				{
					System.out.println("Error: The label " + value + " is already defined in the program.");
					correct = false;
					break;
				}
				// Otherwise add the value of the label to the lists.
				else
				{
					labelTotal.add(value);
					labelCheck.get(subNumber).add(value);
				}
			}
			/* Add the values that branch and jump temporarily to array. They will be checked
			 * at the end of a subroutine to make sure that it is valid 
			 */
			else if(type.equals("-Jump-") || type.equals("-Branch-"))
			{
				// Check the next value after jump or branch to see which label to move to.
				i++;
				value = tokens.get(i).getValue();
				
				// Add the value to the array list jump check.
				jumpCheck.add(value);
			}
			/* Check for the end of a subroutine and increase the counter by one. This will help
			 * keep track of areas that can be branched or jumped to. 
			 */
			else if(type.equals("-End-") || i == (tokens.size()-1))
			{
				// Check first if this label is defined in the program and subroutine.
				boolean inside = true;
				
				if(jumpCheck.size() != 0)
				{
					// Set to false if jump check has values and make sure they are defined in the subroutine.
					inside = false;
					
					// Loop through the jumps in this routine to check if the label is there as well.
					for(int k = 0; k < jumpCheck.size(); k++)
					{
						for(int l = 0; l < labelCheck.get(subNumber).size(); l++)
						{
							if(labelCheck.get(subNumber).get(l).equals(jumpCheck.get(k)))
								inside = true;
						}
					}
					// If it is not inside then there is a static rule error.
					if(!inside)
					{
						System.out.println("Error: The jump/branch statement is jumping to a label that is not defined" +
								"or a label that is outside the subroutine.");
						correct = false;
						break;
					}
				}
				
				// Increment the subNumber to collect labels for the next sub routine.
				subNumber = subNumber + 1;
				labelCheck.add(new ArrayList<String>());
				
				// Clear the array for the next subroutine.
				jumpCheck.clear();	
			}
			else
			{
				//Skip this token because it is not a part of the static semantic rules.
			}
		}
		
		return correct;
	}
	/* The runtime method uses all the functions in the built in library. The library functions manipulate
	 * a stack and a global array that are part of the class. The try/catch block will be used to catch
	 * any runtime errors that might pop up during the program. If it is successful it will run through the
	 * program as if it were a normal java program.
	 */
	public static void runTime(ArrayList<Pair> tokens) throws IOException
	{
		// This array list will hold all the subroutine names found in the program.
		ArrayList<ArrayList<Pair>> subRout = new ArrayList<ArrayList<Pair>>();
		// THis array is to hold each of the jump and branch functions for a routine.
		ArrayList<Pair> mainFunc = new ArrayList<Pair>();
		
		// Holds the token currently being pointed to.
		Pair tok;
		// Holds the information of which subroutine is currently being analyzed.
		int subNumber = 0;
		subRout.add(new ArrayList<Pair>());
		
		// Partition each section of the code into subroutine blocks and a main function.
		for(int i = 0; i < tokens.size(); i++)
		{
			// Get the token type and value.
			tok = tokens.get(i);
			
			/* Checks the name of the subroutine to make sure that it has only been defined once
			 * and that it is not the same as any built in functions.
			 */
			if(tok.getToken().equals("-Sub-"))
			{
				i++; 
				tok = tokens.get(i);
				while(!tok.getToken().equals("-End-"))
				{
					// Move to the next token and add that to the array list for this subroutine.
					subRout.get(subNumber).add(tok);
					i++; 
					tok = tokens.get(i);
				}
				subNumber = subNumber + 1; 
				subRout.add(new ArrayList<Pair>());
			}
			// Otherwise it must be the main method so it creates that section here.
			else
			{
				while(i < tokens.size())
				{
					// Get the information for the token and add the value to the function main.
					tok = tokens.get(i);
					mainFunc.add(tok);
					i++;
				}
			}
		}
		
		// NOW IT IS TIME TO RUN THE PROGRAM TO ENSURE CORRECT SEMANTICS // 
		
		// Declare and initialize a new run-time library to use with the program.
		RunLibrary program = new RunLibrary();
		
		// Holds the type of the token currently being analyzed.
		String type;
		// Holds the value of the token currently being analyzed.
		String value;
		
		for(int i = 0; i < mainFunc.size(); i++)
		{
			// Get the information about the type and value of the token currently pointed to.
			type = mainFunc.get(i).getToken();
			value = mainFunc.get(i).getValue();
			
			/* Try the appropriate function and catch any errors. If an error is returned than the run
			 * time semantic check has failed and the appropraite message is return and the program 
			 * returns to the main method.
			 */
			try
			{	
				// If the token is a number than it has to be pushed onto the stack.
				if(type.equals("-Number-"))
				{
					program.addTo(type, value);
				}
				// If the token is a string than it has to be pushed onto the stack.
				else if(type.equals("-String-"))
				{
					program.addTo(type, value);
				}
				// If the token is a character than it has to be pushed onto the stack.
				else if(type.equals("-Character-"))
				{
					program.addTo(type, value);
				}
				// If the token is a label no action is required at this point.
				else if(type.equals("-Label-"))
				{
					// Do nothing with a label since this is just points to a place in the program sequence.
				}
				// If the token is jump than move to the label that jump is telling the program to.
				else if(type.equals("-Jump-"))
				{
					// Get the next token that is the place where jump jumps to.
					i++;
					type = mainFunc.get(i).getToken();
					value = mainFunc.get(i).getValue();
					// Returns an integer of the position of the label and moves i to that location.
					i = program.jump(mainFunc, value);
				}
				// If the token is branch than move to the label if the value on the top of the stack is true.
				else if(type.equals("-Branch-"))
				{
					// Get the next token that is the place where branch jumps to.
					i++;
					type = mainFunc.get(i).getToken();
					value = mainFunc.get(i).getValue();
					// Returns an integer of the position of the label and moves i to that location if >= 0.
					int place = program.branch(mainFunc, value);
					if(place < 0)
					{
						// Do nothing because branch wasn't done
					}
					else
					{
						i = place;
					}
				}
				// If the token is an identifier it can be one of many built in or defined functions. Check.
				else if(type.equals("-Identifier-"))
				{
					// Add the top two values on the stack.
					if(value.equals("+"))
						program.add();
					// Subtract the top two values on the stack.
					else if(value.equals("-"))
						program.subtract();
					// Multiply the top two values on the stack.
					else if(value.equals("*"))
						program.multiply();
					// Divide and mode the top two values on the stack.
					else if(value.equals("/"))
						program.divide();
					// Negate the top value on the stack.
					else if(value.equals("neg"))
						program.negative();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("<"))
						program.lessThan();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals(">"))
						program.greaterThan();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("<="))
						program.lessThanEq();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals(">="))
						program.greaterThanEq();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("="))
						program.equal();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("!="))
						program.notEqual();
					// Return a boolean of the logical operation of the top two values on the stack.
					else if(value.equals("and"))
						program.and();
					// Return a boolean of the logical operation of the top two values on the stack.
					else if(value.equals("or"))
						program.or();
					// Return a boolean of the logical operation of the top value on the stack.
					else if(value.equals("not"))
						program.not();
					// Concatenate the top two values on the stack.
					else if(value.equals("concat"))
						program.concat();
					// Get a substring of the top value on the stack.
					else if(value.equals("substr"))
						program.substr();
					// Get the length of the string on top of the stack.
					else if(value.equals("length"))
						program.length();
					// Get a character from the string on top of the stack.
					else if(value.equals("getchar"))
						program.getChar();
					// Put a character in the top string in the stack.
					else if(value.equals("putchar"))
						program.putChar();
					// Change a boolean or a number to a string on the stack.
					else if(value.equals("tostring"))
						program.tostring();
					// Convert the string on top of the stack to an integer.
					else if(value.equals("toint"))
						program.toint();
					// Convert the string on top of the stack to a boolean.
					else if(value.equals("tobool"))
						program.toBool();
					// Drop the topmost item on the stack.
					else if(value.equals("drop"))
						program.drop();
					// Duplicate the topmost item on the stack.
					else if(value.equals("dup"))
						program.dup();
					// Rotate some of the items on top of the stack.
					else if(value.equals("rot"))
						program.rot();
					// Swap some of the items on top of the stack.
					else if(value.equals("swap"))
						program.swap();
					// Read a character from standard input.
					else if(value.equals("read"))
						program.read();
					// Write a character to standard output.
					else if(value.equals("write"))
						program.write();
					// Push the boolean value true onto the stack.
					else if(value.equals("true"))
						program.addTo(type, value);
					// Push the boolean value false onto the stack.
					else if(value.equals("false"))
						program.addTo(type, value);
					// Load a reference into memory.
					else if(value.equals("load"))
						program.load();
					// Save a value to referenced memory location.
					else if(value.equals("save"))
						program.save();
					// Reference a location on the global array and push it onto the stack.
					else if(value.equals("ref"))
						program.ref();
					// If it not a built in function it must be a created one.
					else
					{
						// Moves through all the defined subroutines to find the correct one.
						for(int j = 0; j < subRout.size(); j++)
						{
							// Finds the subroutine that has the same name as the value calling it.
							if(subRout.get(j).get(0).getValue().equals(value))
							{
								program.subroutine(subRout.get(j), subRout);
								j = subRout.size();
							}
						}
					}
				}
			
				// If the token is a variable if must be pushed onto the stack.
				else
				{
					// The actual value doesn't matter since the stack will keep track from now on.
					program.addReference(value);
				}
			}
			/* There will be a number of errors that need to be caught by these statements. They
			 * will let us know if the program is semantically correct or if a runtime error has
			 * occurred during the interpretation.
			 */
			catch(Throwable e)
			{
				System.out.println("\nError: the program is semantically incorrect due to the following." 
						           + "\n" + e);
				System.exit(0);
			}
		}
	}
}
